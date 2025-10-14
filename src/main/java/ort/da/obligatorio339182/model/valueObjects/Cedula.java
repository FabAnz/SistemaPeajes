package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;

@Value
public class Cedula {
	String valor;

	public Cedula(String valor) {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) {
		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException("La cédula no puede estar vacía");
		}
		// TODO: Implementar validación de formato de cédula uruguaya (8 dígitos con/sin puntos y guiones)
	}
}
