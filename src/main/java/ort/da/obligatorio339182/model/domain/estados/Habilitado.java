package ort.da.obligatorio339182.model.domain.estados;

public class Habilitado extends Estado {

	/**
	 * Usuario habilitado puede recibir bonificaciones
	 */
	@Override
	public boolean puedeAsignarBonificaciones() {
		return true;
	}

	@Override
	public String getNombre() {
		return "Habilitado";
	}

}
