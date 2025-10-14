package ort.da.obligatorio339182.model.domain.usuarios;

import java.util.Set;
import ort.da.obligatorio339182.model.domain.Permiso;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Administrador extends Usuario {

	private static final Set<Permiso> permisoAdmin = Set.of();

	public Administrador(int id, String nombreCompleto, Contrasenia contrasenia, Cedula cedula) {
		super(id, nombreCompleto, contrasenia, cedula);
	}

	@Override
	public void validar() {
	}

	@Override
	public boolean tienePermiso(Permiso permiso) {
		return permisoAdmin.contains(permiso);
	}

}
