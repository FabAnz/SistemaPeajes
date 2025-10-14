package ort.da.obligatorio339182.model.domain.usuarios;

import ort.da.obligatorio339182.model.domain.Permiso;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Usuario {
	private int id;
	private String nombreCompleto;
	private Contrasenia contrasenia;
	private Cedula cedula;

	public abstract void validar();

	public abstract boolean tienePermiso(Permiso permiso);

}
