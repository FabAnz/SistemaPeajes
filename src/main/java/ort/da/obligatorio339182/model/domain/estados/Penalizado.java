package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;
import ort.da.obligatorio339182.exceptions.AppException;

@EqualsAndHashCode
public class Penalizado implements Estado {
	/* Penalizado: El usuario puede ingresar al sistema, pero no se le registran notificaciones.
	Puede realizar tránsitos, pero no aplican las bonificaciones que tenga asignadas. */

	@Override
	public void validarAccesoAlSistema() throws AppException {
		// No lanza excepción - usuario penalizado puede ingresar
	}
	
	@Override
	public void validarPuedeRealizarTransitos() throws AppException {
		// No lanza excepción - usuario penalizado puede realizar tránsitos
	}

	@Override
	public boolean aplicanBonificaciones(){
		return false;
	}

	@Override
	public boolean recibeNotificaciones(){
		return false;
	}

	@Override
	public String getNombre(){
		return "Penalizado";
	}

}
