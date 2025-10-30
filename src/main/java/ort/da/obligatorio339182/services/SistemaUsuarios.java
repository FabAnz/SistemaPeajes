package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.valueObjects.Matricula;

@Service
class SistemaUsuarios {
	private final Fachada fachada;
	private List<Usuario> usuarios;

	SistemaUsuarios(@Lazy Fachada fachada) {
		this.fachada = fachada;
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
			try {
				propietario.getEstado().validarAccesoAlSistema();
			} catch (AppException e) {
				throw new UnauthorizedException(e.getMessage());
			}
		}
		
		// Luego verificar el permiso específico
		if (!usuario.tienePermiso(permisoRequerido)) {
			throw new UnauthorizedException("Usuario no tiene permiso para acceder a este recurso");
		}
		
		return usuario;
	}

	Propietario getPropietarioPorMatricula(Matricula matricula) throws AppException {
		Vehiculo vehiculo = fachada.getVehiculoPorMatricula(matricula);
		Propietario propietario = vehiculo.getPropietario();
		if(propietario == null) {
			throw new AppException("Propietario no encontrado");
		}
		return propietario;
	}

}
