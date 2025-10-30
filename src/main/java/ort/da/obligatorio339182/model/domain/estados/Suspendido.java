package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Suspendido implements Estado {
	/* Suspendido: El usuario puede ingresar al sistema, pero no puede realizar tránsitos. */

	@Override
	public boolean puedeIngresarAlSistema(){
		return true;
	};

	@Override
	public boolean puedeRealizarTransitos(){
		return false;
	};

	@Override
	public boolean puedeAsignarBonificaciones(){
		return true;
	};

	@Override
	public boolean aplicanBonificaciones(){
		return true;
	};

	@Override
	public boolean recibeNotificaciones(){
		return true;
	};

	@Override
	public String getNombre(){
		return "Suspendido";
	};

}
