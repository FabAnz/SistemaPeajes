package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Deshabilitado implements Estado {

	/*
	 * Deshabilitado: El usuario no puede ingresar al sistema ni puede realizar
	 * tránsitos.
	 * Tampoco se le pueden asignar bonificaciones.
	 */

	@Override
	public boolean puedeIngresarAlSistema() {
		return false;
	}

	@Override
	public boolean puedeRealizarTransitos() {
		return false;
	}

	@Override
	public boolean puedeAsignarBonificaciones() {
		return false;
	}

	@Override
	public boolean aplicanBonificaciones() {
		return false;
	}

	@Override
	public String getNombre() {
		return "Deshabilitado";
	}

	@Override
	public boolean recibeNotificaciones() {
		return false;
	}

}
