package ort.da.obligatorio339182.model.domain.bonifiaciones;

import ort.da.obligatorio339182.model.domain.Transito;

public class Exonerado implements Bonificacion {

	/**
	 * Return 100
	 */
	@Override
	public int getBonificacion(Transito transito) {
		return 100;
	}

	/**
	 * return Exonerado
	 */
	@Override
	public String getNombre() {
		return "Exonerado";
	}

}
