package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;
import ort.da.obligatorio339182.exceptions.AppException;

@EqualsAndHashCode
public class Habilitado implements Estado {
	/* Habilitado: Es el estado por defecto de los propietarios cuando se dan de alta en el
	sistema. El propietario tiene todas las funcionalidades habilitadas. */

	@Override
	public void validarAccesoAlSistema() throws AppException {
		// No lanza excepción - usuario habilitado puede ingresar
	}
	
	@Override
	public void validarPuedeRealizarTransitos() throws AppException {
		// No lanza excepción - usuario habilitado puede realizar tránsitos
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
		return "Habilitado";
	}

}
