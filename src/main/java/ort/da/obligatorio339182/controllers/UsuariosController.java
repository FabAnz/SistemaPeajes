package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

	private Fachada fachada;

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
