package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
public class Notificacion {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String mensaje;

	public Notificacion(String mensaje) {
		this.id = ++nextId;
		this.mensaje = mensaje;
	}

	public void validar() {

	}

}
