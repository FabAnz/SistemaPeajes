package ort.da.obligatorio339182.model.domain.estados;

public abstract class Estado {
	// Singleton de cada estado - Inicialización correcta
	private static final Estado habilitado = new Habilitado();
	private static final Estado deshabilitado = new Deshabilitado();
	private static final Estado suspendido = new Suspendido();
	private static final Estado penalizado = new Penalizado();

	// Métodos para obtener las instancias únicas
	public static Estado habilitado() {
		return habilitado;
	}

	public static Estado deshabilitado() {
		return deshabilitado;
	}

	public static Estado suspendido() {
		return suspendido;
	}

	public static Estado penalizado() {
		return penalizado;
	}

	/**
	 * Indica si el usuario puede ingresar al sistema
	 * Por defecto retorna true, cada estado sobrescribe si necesita
	 */
	public boolean puedeIngresarAlSistema() {
		return true;
	}

	/**
	 * Indica si el usuario puede realizar tránsitos
	 * Por defecto retorna true, cada estado sobrescribe si necesita
	 */
	public boolean puedeRealizarTransitos() {
		return true;
	}

	/**
	 * Indica si se pueden asignar bonificaciones al usuario
	 * Por defecto retorna false, solo Habilitado lo sobrescribe
	 */
	public boolean puedeAsignarBonificaciones() {
		return false;
	}

	/**
	 * Retorna el nombre del estado
	 */
	public abstract String getNombre();

}
