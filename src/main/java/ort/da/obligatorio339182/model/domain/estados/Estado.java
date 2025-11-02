package ort.da.obligatorio339182.model.domain.estados;

import ort.da.obligatorio339182.exceptions.AppException;

public interface Estado {

	/**
	 * Valida que el usuario pueda ingresar al sistema.
	 * Lanza AppException si el estado no permite el acceso.
	 */
	public void validarAccesoAlSistema() throws AppException;
	
	/**
	 * Valida que el usuario pueda realizar tránsitos.
	 * Lanza AppException si el estado no permite realizar tránsitos.
	 */
	public void validarPuedeRealizarTransitos() throws AppException;

	/**
	 * Indica si en este estado se aplican las bonificaciones asignadas.
	 */
	public boolean aplicanBonificaciones();

	/**
	 * Indica si en este estado se generan notificaciones al usuario.
	 */
	public boolean recibeNotificaciones();

	public String getNombre();

}
