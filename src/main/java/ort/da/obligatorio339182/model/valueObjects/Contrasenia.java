package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;
import ort.da.obligatorio339182.exceptions.AppException;

@Value
public class Contrasenia {
	String valor;

	public Contrasenia(String valor) throws AppException {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) throws AppException {
		if (valor == null || valor.isBlank()) {
			throw new AppException("La contraseña no puede estar vacía");
		}
		
		// TODO: Implementar validación de requisitos de contraseña (longitud mínima, caracteres especiales, etc.)
		// Validar longitud mínima
		if (valor.length() < 8) {
			throw new AppException("La contraseña debe tener al menos 8 caracteres");
		}
		
		// Validar longitud máxima
		if (valor.length() > 50) {
			throw new AppException("La contraseña no puede tener más de 50 caracteres");
		}
		
		// Validar que contenga al menos una letra mayúscula
		if (!valor.matches(".*[A-Z].*")) {
			throw new AppException("La contraseña debe contener al menos una letra mayúscula");
		}
		
		// Validar que contenga al menos una letra minúscula
		if (!valor.matches(".*[a-z].*")) {
			throw new AppException("La contraseña debe contener al menos una letra minúscula");
		}
		
		// Validar que contenga al menos un número
		if (!valor.matches(".*[0-9].*")) {
			throw new AppException("La contraseña debe contener al menos un número");
		}
		
		// Validar que contenga al menos un carácter especial
		if (!valor.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
			throw new AppException("La contraseña debe contener al menos un carácter especial (!@#$%^&*()_+-=[]{}; ':\"\\|,.<>/?)");
		}
		
		// Validar que no contenga espacios
		if (valor.contains(" ")) {
			throw new AppException("La contraseña no puede contener espacios");
		}
	}

	public boolean esCorrecta(String contrasenia) {
		return this.valor.equals(contrasenia);
	}
}
