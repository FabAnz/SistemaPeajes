package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

@Service
class SistemaUsuarios {
	private List<Usuario> usuarios;

	SistemaUsuarios() {
		this.usuarios = new ArrayList<Usuario>();
	}

	void agregarUsuario(Usuario usuario) throws AppException {
		if (!validarCedulaUnica(usuario.getCedula())) {
			throw new AppException("La cédula ya está en uso");
		}
		usuario.validar();
		this.usuarios.add(usuario);
	}

	Usuario getUsuarioPorCedula(String cedula) {
		for (Usuario usuario : usuarios) {
			if (usuario.esSuCedula(cedula)) {
				return usuario;
			}
		}
		return null;
	}

	private boolean validarCedulaUnica(Cedula cedula) {
		for (Usuario usuario : usuarios) {
			if (usuario.getCedula().equals(cedula)) {
				return false;
			}
		}
		return true;
	}

	Usuario login(String cedula, String contrasenia) throws AppException {
		Usuario usuario = getUsuarioPorCedula(cedula);

		if (usuario == null || !usuario.accesoPermitido(contrasenia)) {
			throw new AppException("Usuario y/o contraseña incorrectos");
		}

		return usuario;
	}

	/**
	 * Obtiene un usuario por su ID
	 * @param id El ID del usuario
	 * @return El usuario si existe, null en caso contrario
	 */
	Usuario getUsuarioPorId(int id) {
		for (Usuario usuario : usuarios) {
			if (usuario.getId() == id) {
				return usuario;
			}
		}
		return null;
	}

	Usuario validarPermiso(Integer usuarioId, Permiso permisoRequerido) throws UnauthorizedException {
		Usuario usuario = getUsuarioPorId(usuarioId);
		if (usuario == null) {
			throw new UnauthorizedException("Usuario no encontrado");
		}
		
		// Verificar primero si el propietario puede ingresar al sistema
		if(usuario instanceof Propietario) {
			Propietario propietario = (Propietario) usuario;
			if(!propietario.puedeIngresarAlSistema()) {
				throw new UnauthorizedException("Usuario deshabilitado, no puede ingresar al sistema");
			}
		}
		
		// Luego verificar el permiso específico
		if (!usuario.tienePermiso(permisoRequerido)) {
			throw new UnauthorizedException("Usuario no tiene permiso para acceder a este recurso");
		}
		
		return usuario;
	}

}
