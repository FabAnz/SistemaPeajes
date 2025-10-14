package ort.da.obligatorio339182.model.domain.estados;

public class Suspendido extends Estado {

	/**
	 * Usuario suspendido NO puede realizar tránsitos
	 */
	@Override
	public boolean puedeRealizarTransitos() {
		return false;
	}

	@Override
	public String getNombre() {
		return "Suspendido";
	}

}
