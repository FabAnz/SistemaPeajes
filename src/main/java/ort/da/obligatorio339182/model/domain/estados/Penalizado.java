package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Penalizado implements Estado {
	/* Penalizado: El usuario puede ingresar al sistema, pero no se le registran notificaciones.
	Puede realizar tránsitos, pero no aplican las bonificaciones que tenga asignadas. */

	@Override
	public boolean puedeIngresarAlSistema(){
		return true;
	};

	@Override
	public boolean puedeRealizarTransitos(){
		return true;
	};

	@Override
	public boolean puedeAsignarBonificaciones(){
		return true;
	};

	@Override
	public boolean aplicanBonificaciones(){
		return false;
	};

	@Override
	public boolean recibeNotificaciones(){
		return false;
	};

	@Override
	public String getNombre(){
		return "Penalizado";
	};
}
