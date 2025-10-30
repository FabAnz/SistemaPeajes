package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;
import ort.da.obligatorio339182.exceptions.AppException;

@EqualsAndHashCode
public class Suspendido implements Estado {
	/* Suspendido: El usuario puede ingresar al sistema, pero no puede realizar tránsitos. */

	@Override
	public void validarAccesoAlSistema() throws AppException {
		// No lanza excepción - usuario suspendido puede ingresar
	}
	
	@Override
	public void validarPuedeRealizarTransitos() throws AppException {
		throw new AppException("Usuario suspendido no puede realizar tránsitos");
	}

	@Override
	public boolean aplicanBonificaciones(){
		return true;
	}

	@Override
	public boolean recibeNotificaciones(){
		return true;
	}

	@Override
	public String getNombre(){
		return "Suspendido";
	}

}
