package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;

@Value
public class Matricula {
	String valor;

	public Matricula(String valor) {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) {
		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException("La matrícula no puede estar vacía");
		}
		// TODO: Implementar validación de formato de matrícula uruguaya (ej: ABC1234)
	}
}
