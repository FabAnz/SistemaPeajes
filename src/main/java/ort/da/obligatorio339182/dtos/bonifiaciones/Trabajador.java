package ort.da.obligatorio339182.dtos.bonifiaciones;

public class Trabajador extends Bonificacion {

	/**
	 * If es día de semana
	 * Return 80
	 * Else
	 * Return 0
	 */
	@Override
	public int getBonificacion() {
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
