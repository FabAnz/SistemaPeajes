package ort.da.obligatorio339182.model.domain.usuarios;

import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.exceptions.AppException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class Usuario {
	private int id;
	@NonNull private String nombreCompleto;
	@Getter(AccessLevel.PROTECTED)
	@NonNull private Contrasenia contrasenia;
	@NonNull private Cedula cedula;

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
