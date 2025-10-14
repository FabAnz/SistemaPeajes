package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.valueObjects.Matricula;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehiculo {

	private int id;
	private String modelo;
	private String color;
	private Categoria categoria;
	private Matricula matricula;

	public void validar() {

	}

}
