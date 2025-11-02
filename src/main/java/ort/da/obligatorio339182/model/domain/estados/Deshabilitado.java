package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;
import ort.da.obligatorio339182.exceptions.AppException;

@EqualsAndHashCode
public class Deshabilitado implements Estado {

	/*
	 * Deshabilitado: El usuario no puede ingresar al sistema ni puede realizar
	 * tránsitos.
	 * Tampoco se le pueden asignar bonificaciones.
	 */

	@Override
	public void validarAccesoAlSistema() throws AppException {
		throw new AppException("Usuario deshabilitado, no puede ingresar al sistema");
	}
	
	@Override
	public void validarPuedeRealizarTransitos() throws AppException {
		throw new AppException("Usuario deshabilitado no puede realizar tránsitos");
	}

	@Override
	public boolean aplicanBonificaciones() {
		return false;
	}

	@Override
	public boolean recibeNotificaciones() {
		return false;
	}

	@Override
	public String getNombre() {
		return "Deshabilitado";
	}

}
