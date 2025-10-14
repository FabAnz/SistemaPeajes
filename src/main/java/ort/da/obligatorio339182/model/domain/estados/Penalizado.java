package ort.da.obligatorio339182.model.domain.estados;

public class Penalizado extends Estado {

	/**
	 * Usuario penalizado mantiene comportamiento por defecto
	 * (puede ingresar y realizar tránsitos, pero no recibir bonificaciones)
	 */
	@Override
	public String getNombre() {
		return "Penalizado";
	}

}
