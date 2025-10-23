package ort.da.obligatorio339182.model.domain.usuarios;

import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.exceptions.AppException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;

@Data
@NoArgsConstructor
public abstract class Usuario {
	private static int nextId = 0;
	
	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String nombreCompleto;
	@Getter(AccessLevel.PROTECTED)
	private Contrasenia contrasenia;
	private Cedula cedula;

	public Usuario(String nombreCompleto, Contrasenia contrasenia, Cedula cedula) {
		this.id = ++nextId;
		this.nombreCompleto = nombreCompleto;
		this.contrasenia = contrasenia;
		this.cedula = cedula;
	}

	public abstract void validar() throws AppException;

	public abstract boolean tienePermiso(Permiso permiso);


	public boolean accesoPermitido(String contrasenia) throws AppException {
		return this.contraseniaCorrecta(contrasenia);
	}

	private boolean contraseniaCorrecta(String contrasenia) {
		return this.contrasenia.esCorrecta(contrasenia);
	}

	public boolean esSuCedula(String cedula) {
		return this.cedula.esIgualA(cedula);
	}

}
