package ort.da.obligatorio339182.model.domain.bonifiaciones;

import ort.da.obligatorio339182.model.domain.Transito;

public class Trabajador implements Bonificacion {

	/**
	 * If es día de semana
	 * Return 80
	 * Else
	 * Return 0
	 */
	@Override
	public int getBonificacion(Transito transito) {
		int dia = transito.getFechaHora().getDayOfWeek().getValue();
		if (dia >= 1 && dia <= 5) {
			return 80;
		}
		return 0;
	}

	/**
	 * return Trabajador
	 */
	@Override
	public String getNombre() {
		return "Trabajador";
	}

}
