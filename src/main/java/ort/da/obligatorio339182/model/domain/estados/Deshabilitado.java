package ort.da.obligatorio339182.model.domain.estados;

public class Deshabilitado extends Estado {

	/**
	 * Usuario deshabilitado NO puede ingresar al sistema
	 */
	@Override
	public boolean puedeIngresarAlSistema() {
		return false;
	}

	/**
	 * Usuario deshabilitado NO puede realizar tránsitos
	 */
	@Override
	public boolean puedeRealizarTransitos() {
		return false;
	}

	@Override
	public String getNombre() {
		return "Deshabilitado";
	}

}
