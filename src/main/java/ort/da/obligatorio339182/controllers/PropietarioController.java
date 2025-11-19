package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.observer.Observable;
import ort.da.obligatorio339182.observer.Observador;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario.Evento;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.dtos.PropietarioInfoDTO;
import ort.da.obligatorio339182.dtos.BonificacionAsignadaDTO;
import ort.da.obligatorio339182.dtos.VehiculoPropietarioDTO;
import ort.da.obligatorio339182.dtos.TransitoPropietarioDTO;
import ort.da.obligatorio339182.model.domain.Transito;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import ort.da.obligatorio339182.model.domain.Notificacion;
import ort.da.obligatorio339182.dtos.NotificacionDTO;
import ort.da.obligatorio339182.exceptions.AppException;
import org.springframework.context.annotation.Scope;
import ort.da.obligatorio339182.utils.ConexionNavegador;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/propietario")
@Scope("session")
public class PropietarioController extends BaseController implements Observador {

	private Propietario propietario;
	private List<BonificacionAsignada> bonificaciones = new ArrayList<>();
	private final ConexionNavegador conexionNavegador;

	public PropietarioController(Fachada fachada, @Autowired ConexionNavegador conexionNavegador) {
		super(fachada);
		this.conexionNavegador = conexionNavegador;
	}

	@GetMapping("/dashboard")
	public List<RespuestaDTO> cargarDashboardPropietario(HttpSession session) throws UnauthorizedException {
		Integer usuarioId = validarSesion(session);
		// Validar sesión y permisos
		Usuario usuario = fachada.validarPermiso(usuarioId, Permiso.PROPIETARIO_DASHBOARD);

		fachada.agregarObservador(this);

		// Castear a Propietario (ya sabemos que tiene el permiso PROPIETARIO_DASHBOARD)
		propietario = (Propietario) usuario;
		propietario.agregarObservador(this);

		// Crear DTO con la información del propietario
		PropietarioInfoDTO infoDTO = new PropietarioInfoDTO(propietario);

		// HU 2.2: Obtener bonificaciones asignadas al propietario
		bonificaciones = fachada.getBonificacionesPorPropietario(propietario);

		// HU 2.3: Obtener vehículos del propietario con cálculos
		List<Vehiculo> vehiculos = propietario.getVehiculos();
		List<VehiculoPropietarioDTO> vehiculosDTO = new ArrayList<>();

		// Construir cada VehiculoDTO con datos calculados desde Fachada
		for (Vehiculo v : vehiculos) {
			int cantidad = fachada.cantidadTransitosPorPropietarioYVehiculo(propietario, v);
			int monto = fachada.montoTotalPorPropietarioYVehiculo(propietario, v);
			vehiculosDTO.add(new VehiculoPropietarioDTO(v, cantidad, monto));
		}

		// HU 2.4: Obtener tránsitos del propietario
		List<Transito> transitos = propietario.getTransitos();
		List<TransitoPropietarioDTO> transitosDTO = new ArrayList<>();

		// Construir cada TransitoDTO con información de bonificación aplicada
		for (Transito t : transitos) {
			BonificacionAsignada bonif = fachada.getBonificacionEnPuesto(
					propietario, t.getPuesto());
			transitosDTO.add(new TransitoPropietarioDTO(t, bonif));
		}

		// HU 2.5: Obtener notificaciones del propietario
		List<Notificacion> notificaciones = propietario.getNotificacionesOrdenadas();

		return RespuestaDTO.lista(
				new RespuestaDTO("propietario", infoDTO),
				new RespuestaDTO("bonificaciones", BonificacionAsignadaDTO.list(bonificaciones)),
				new RespuestaDTO("vehiculos", vehiculosDTO),
				new RespuestaDTO("transitos", transitosDTO),
				new RespuestaDTO("notificaciones", NotificacionDTO.list(notificaciones)));
	}

	@PreDestroy
	public void preDestroy() {
		if (this.propietario != null)
			this.propietario.quitarObservador(this);
		fachada.quitarObservador(this);
		this.conexionNavegador.cerrarConexion();
	}

	@DeleteMapping("/notificaciones")
	public List<RespuestaDTO> borrarNotificaciones(HttpSession session) throws UnauthorizedException, AppException {
		Integer usuarioId = validarSesion(session);
		Usuario usuario = fachada.validarPermiso(usuarioId, Permiso.BORRAR_NOTIFICACIONES);
		Propietario propietario = (Propietario) usuario;
		propietario.borrarNotificaciones();

		List<Notificacion> notificaciones = propietario.getNotificacionesOrdenadas();

		return RespuestaDTO.lista(
				new RespuestaDTO("mensaje", "Notificaciones borradas correctamente"),
				new RespuestaDTO("notificaciones", NotificacionDTO.list(notificaciones)));
	}

	@Override
	public void actualizar(Object evento, Observable origen) {

		if (Evento.ESTADO_CAMBIADO.equals(evento)) {
			Propietario propietarioActualizado = (Propietario) origen;
			propietario = propietarioActualizado;
			List<RespuestaDTO> respuesta = RespuestaDTO.lista(
					new RespuestaDTO("propietario", new PropietarioInfoDTO(propietario)));
			conexionNavegador.enviarJSON(respuesta);
		}

		if (Evento.TRANSITO_AGREGADO.equals(evento)) {
			Propietario propietarioActualizado = (Propietario) origen;
			propietario = propietarioActualizado;
			List<TransitoPropietarioDTO> transitosDTO = new ArrayList<>();

			// Construir cada TransitoDTO con información de bonificación aplicada
			for (Transito t : propietario.getTransitos()) {
				BonificacionAsignada bonif = fachada.getBonificacionEnPuesto(
						propietario, t.getPuesto());
				transitosDTO.add(new TransitoPropietarioDTO(t, bonif));
			}
			List<RespuestaDTO> respuesta = RespuestaDTO.lista(
					new RespuestaDTO("propietario", new PropietarioInfoDTO(propietario)),
					new RespuestaDTO("transitos", transitosDTO));
			conexionNavegador.enviarJSON(respuesta);
		}

		if (Evento.NOTIFICACION_AGREGADA.equals(evento)) {
			Propietario propietarioActualizado = (Propietario) origen;
			propietario = propietarioActualizado;
			List<Notificacion> notificaciones = propietario.getNotificacionesOrdenadas();
			List<RespuestaDTO> respuesta = RespuestaDTO.lista(
					new RespuestaDTO("notificaciones", NotificacionDTO.list(notificaciones)));
			conexionNavegador.enviarJSON(respuesta);
		}

		if (Fachada.Evento.BONIFICACION_ASIGNADA.equals(evento)) {
			bonificaciones = fachada.getBonificacionesPorPropietario(propietario);
			List<RespuestaDTO> respuesta = RespuestaDTO.lista(
					new RespuestaDTO("bonificaciones", BonificacionAsignadaDTO.list(bonificaciones)));
			conexionNavegador.enviarJSON(respuesta);
		}
	}

	@GetMapping("/registrarSSE")
	public SseEmitter registrarSSE(HttpSession session) throws UnauthorizedException {
		Integer usuarioId = validarSesion(session);
		fachada.validarPermiso(usuarioId, Permiso.PROPIETARIO_DASHBOARD);

		propietario.agregarObservador(this);
		conexionNavegador.conectarSSE();
		return conexionNavegador.getConexionSSE();
	}

}
