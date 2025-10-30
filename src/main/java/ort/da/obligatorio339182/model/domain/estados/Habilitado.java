package ort.da.obligatorio339182.model.domain.estados;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Habilitado implements Estado {
	/* Habilitado: Es el estado por defecto de los propietarios cuando se dan de alta en el
	sistema. El propietario tiene todas las funcionalidades habilitadas. */

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
		return true;
	};

	@Override
	public boolean recibeNotificaciones(){
		return true;
	};

	@Override
	public String getNombre(){
		return "Habilitado";
	}

}
