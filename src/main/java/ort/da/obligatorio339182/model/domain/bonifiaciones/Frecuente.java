package ort.da.obligatorio339182.model.domain.bonifiaciones;

import ort.da.obligatorio339182.model.domain.Transito;

public class Frecuente implements Bonificacion {

	/**
	 * if vehículo ya paso en el dia
	 * Return 50
	 * Else
	 * Return 0
	 */
	@Override
	public int getBonificacion(Transito transito) {
		if(!transito.isEsPrimerTransitoDelDia()) {
			return 50;
		}
		return 0;
	}

	/**
	 * return Frecuente
	 */
	@Override
	public String getNombre() {
		return "Frecuente";
	}

}
