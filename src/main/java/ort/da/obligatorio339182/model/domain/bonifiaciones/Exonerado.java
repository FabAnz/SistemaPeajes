package ort.da.obligatorio339182.model.domain.bonifiaciones;

public class Exonerado extends Bonificacion {

	/**
	 * return 100;
	 */
	@Override
	public int getBonificacion() {
		return 0;
	}

	/**
	 * return Exonerado
	 */
	@Override
	public String getNombre() {
		return "Exonerado";
	}

}
