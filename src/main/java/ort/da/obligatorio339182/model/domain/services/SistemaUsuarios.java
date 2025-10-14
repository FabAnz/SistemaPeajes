package ort.da.obligatorio339182.model.domain.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.dtos.LoginDTO;

@Service
public class SistemaUsuarios {
	private static int s_idUsuario;
	private List<Usuario> usuarios;

	public SistemaUsuarios() {
		s_idUsuario = 0;
		this.usuarios = new ArrayList<Usuario>();
	}

	public void crearUsuario(Cedula cedula, String nombre) {

	}

	public Usuario getUsuarioPorCedula(Cedula cedula) {
		return null;
	}

	private boolean validarCedulaUnica(Cedula cedula) {
		return false;
	}

	public void login(LoginDTO usuario) {

	}

}
