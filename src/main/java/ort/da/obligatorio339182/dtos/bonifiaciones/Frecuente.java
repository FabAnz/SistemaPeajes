package ort.da.obligatorio339182.dtos.bonifiaciones;

public class Frecuente extends Bonificacion {

	/**
	 * if vehículo ya paso en el dia
	 * Return 50
	 * Else
	 * Return 0
	 */
	@Override
	public int getBonificacion() {
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
