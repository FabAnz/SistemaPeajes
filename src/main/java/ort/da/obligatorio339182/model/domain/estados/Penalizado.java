package ort.da.obligatorio339182.model.domain.estados;

public class Penalizado extends Estado {

	/**
	 * Usuario penalizado puede ingresar y realizar tránsitos,
	 * pero NO se le aplican bonificaciones ni recibe notificaciones
	 */

	/**
	 * Usuario penalizado NO tiene bonificaciones aplicadas en tránsitos
	 */
	@Override
	public boolean aplicanBonificaciones() {
		return false;
	}

	/**
	 * Usuario penalizado NO recibe notificaciones
	 */
	@Override
	public boolean recibeNotificaciones() {
		return false;
	}

	@Override
	public String getNombre() {
		return "Penalizado";
	}

}
