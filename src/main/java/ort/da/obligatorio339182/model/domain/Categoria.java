package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

	private int id;
	private String nombre;

	public void validar() {
	}
}
