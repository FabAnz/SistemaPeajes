package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
public class Categoria {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String nombre;

	public Categoria(String nombre) {
		this.id = ++nextId;
		this.nombre = nombre;
	}

	public void validar() {
	}
}
