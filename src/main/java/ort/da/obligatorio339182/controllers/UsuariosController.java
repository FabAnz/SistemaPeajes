package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.ArrayList;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.dtos.PropietarioInfoDTO;
import ort.da.obligatorio339182.dtos.BonificacionAsignadaDTO;
import jakarta.servlet.http.HttpSession;

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
		List<BonificacionAsignada> bonificaciones = fachada.getBonificacionesPorPropietario(usuarioId);
		
		return RespuestaDTO.lista(
			new RespuestaDTO("propietario", infoDTO),
			new RespuestaDTO("bonificaciones", BonificacionAsignadaDTO.list(bonificaciones))
			);
	}

	/**
	 * fachada.getBonificacionesPorUsuario
	 * getVehiculosDel Usuario(cedula)
	 * cantidadTransitosPorCedulaYMatricula(cedula, matricula)
	 * gastoEnTransitos(usuario, matricula)
	 */
	/* private List<RespuestaDTO> cargarTablero(UsuarioDTO usuario) {
		return null;
	} */

}
