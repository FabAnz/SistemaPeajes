package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.dtos.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.dtos.PropietarioInfoDTO;
import ort.da.obligatorio339182.dtos.BonificacionAsignadaDTO;
import ort.da.obligatorio339182.dtos.VehiculoPropietarioDTO;
import ort.da.obligatorio339182.dtos.TransitoPropietarioDTO;
import ort.da.obligatorio339182.model.domain.Transito;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
private final Fachada fachada;

	public UsuariosController(Fachada fachada) {
		this.fachada = fachada;
	}

	
	@GetMapping("/dashboard-propietario")
	public List<RespuestaDTO> obtenerInformacionPersonal(HttpSession session) throws UnauthorizedException {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		if(usuarioId == null) {
			throw new UnauthorizedException("No hay sesión activa");
		}
		// Validar sesión y permisos
		Usuario usuario = fachada.validarPermiso(usuarioId, Permiso.PROPIETARIO_DASHBOARD);
		
		// Castear a Propietario (ya sabemos que tiene el permiso PROPIETARIO_DASHBOARD)
		Propietario propietario = (Propietario) usuario;
		
		// Crear DTO con la información del propietario
		PropietarioInfoDTO infoDTO = new PropietarioInfoDTO(propietario);
		
		// HU 2.2: Obtener bonificaciones asignadas al propietario
		List<BonificacionAsignada> bonificaciones = fachada.getBonificacionesPorPropietario(propietario);
		
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
		List<Transito> transitos = fachada.getTransitosPorPropietario(propietario);
		List<TransitoPropietarioDTO> transitosDTO = new ArrayList<>();
		
		// Construir cada TransitoDTO con información de bonificación aplicada
		for (Transito t : transitos) {
			BonificacionAsignada bonif = fachada.getBonificacionEnPuesto(
				propietario, t.getPuesto(), t.getFechaHora()
			);
			transitosDTO.add(new TransitoPropietarioDTO(t, bonif));
		}
		
		return RespuestaDTO.lista(
			new RespuestaDTO("propietario", infoDTO),
			new RespuestaDTO("bonificaciones", BonificacionAsignadaDTO.list(bonificaciones)),
			new RespuestaDTO("vehiculos", vehiculosDTO),
			new RespuestaDTO("transitos", transitosDTO)
		);
	}

	/**
	 * fachada.getBonificacionesPorPropietario
	 * getVehiculosDel Usuario(cedula)
	 * cantidadTransitosPorCedulaYMatricula(cedula, matricula)
	 * gastoEnTransitos(usuario, matricula)
	 */
	/* private List<RespuestaDTO> cargarTablero(UsuarioDTO usuario) {
		return null;
	} */

}
