package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;

@Value
public class Contrasenia {
	String valor;

	public Contrasenia(String valor) {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) {
		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException("La contraseña no puede estar vacía");
		}
		// TODO: Implementar validación de requisitos de contraseña (longitud mínima, caracteres especiales, etc.)
	}
}
