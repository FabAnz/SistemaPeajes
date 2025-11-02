package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;
import ort.da.obligatorio339182.exceptions.AppException;

@Value
public class Matricula {
	String valor;

	public Matricula(String valor) throws AppException {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) throws AppException {
		if (valor == null || valor.isBlank()) {
			throw new AppException("La matrícula no puede estar vacía");
		}
		// TODO: Implementar validación de formato de matrícula uruguaya (ej: ABC1234)
	}
}
