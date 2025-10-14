package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {

	private int id;
	private String mensaje;

	public void validar() {

	}

}
