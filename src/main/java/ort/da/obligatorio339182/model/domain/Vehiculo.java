package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.valueObjects.Matricula;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;


@Data
@NoArgsConstructor
public class Vehiculo {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String modelo;
	private String color;
	private Categoria categoria;
	private Matricula matricula;

	public Vehiculo(String modelo, String color, Categoria categoria, Matricula matricula) {
		this.id = ++nextId;
		this.modelo = modelo;
		this.color = color;
		this.categoria = categoria;
		this.matricula = matricula;
	}

	public void validar() {

	}

}
