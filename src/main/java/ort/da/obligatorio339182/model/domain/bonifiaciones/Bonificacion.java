package ort.da.obligatorio339182.model.domain.bonifiaciones;

public abstract class Bonificacion {
	private static final Exonerado exonerado = new Exonerado();
	private static final Frecuente frecuente = new Frecuente();
	private static final Trabajador trabajador = new Trabajador();

	public abstract int getBonificacion();
	public abstract String getNombre();

	public static Exonerado getExonerado() {
		return exonerado;
	}

	public static Frecuente getFrecuente() {
		return frecuente;
	}

	public static Trabajador getTrabajador() {
		return trabajador;
	}

}
