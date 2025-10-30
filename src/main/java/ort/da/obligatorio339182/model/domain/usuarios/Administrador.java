package ort.da.obligatorio339182.model.domain.usuarios;

import java.util.Set;

import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.exceptions.AppException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Administrador extends Usuario {

	private static final Set<Permiso> permisoAdmin = Set.of(
		Permiso.ADMIN_DASHBOARD,
		Permiso.EMULAR_TRANSITO
	);

	public Administrador(String nombreCompleto, Contrasenia contrasenia, Cedula cedula) {
		super(nombreCompleto, contrasenia, cedula);
	}

	@Override
	public void validar() throws AppException {
		// Validar campos heredados de Usuario
		if (getId() <= 0) {
			throw new AppException("El id debe ser mayor a 0");
		}
		if (getNombreCompleto() == null || getNombreCompleto().isBlank()) {
			throw new AppException("El nombre completo no puede estar vacío");
		}
		if (getContrasenia() == null) {
			throw new AppException("La contraseña no puede ser null");
		}
		if (getCedula() == null) {
			throw new AppException("La cédula no puede ser null");
		}
	}

	@Override
	public boolean tienePermiso(Permiso permiso) {
		return permisoAdmin.contains(permiso);
	}

}
