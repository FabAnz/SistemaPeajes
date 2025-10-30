package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.valueObjects.Matricula;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

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
	private Propietario propietario;

	public Vehiculo(String modelo, String color, Categoria categoria, Matricula matricula) {
		this.id = ++nextId;
		this.modelo = modelo;
		this.color = color;
		this.categoria = categoria;
		this.matricula = matricula;
	}

	public void validar() throws AppException {
		if (modelo == null || modelo.isBlank()) {
			throw new AppException("El modelo del vehículo no puede estar vacío");
		}
		
		if (color == null || color.isBlank()) {
			throw new AppException("El color del vehículo no puede estar vacío");
		}
		
		if (categoria == null) {
			throw new AppException("La categoría del vehículo no puede ser nula");
		}
		
		if (matricula == null) {
			throw new AppException("La matrícula del vehículo no puede ser nula");
		}
		
		// Validar la categoría (delega al experto)
		categoria.validar();
	}

}
