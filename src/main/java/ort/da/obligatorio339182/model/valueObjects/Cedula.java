package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;
import ort.da.obligatorio339182.exceptions.AppException;

@Value
public class Cedula {
	String valor;

	public Cedula(String valor) throws AppException {
		validar(valor);
		this.valor = valor;
	}

	public boolean esIgualA(String cedula) {
		return this.valor.equals(cedula);
	}

	private void validar(String valor) throws AppException {
		if (valor == null || valor.isBlank()) {
			throw new AppException("La cédula no puede estar vacía");
		}
		
		// Validar que tenga 7 u 8 dígitos (Uruguay tiene ambos formatos)
		if (!valor.matches("\\d{7,8}")) {
			throw new AppException("La cédula debe tener 7 u 8 dígitos numéricos (sin puntos ni guiones)");
		}
		
		// Validar dígito verificador usando el algoritmo oficial uruguayo
		if (!validarDigitoVerificador(valor)) {
			throw new AppException("El dígito verificador de la cédula no es válido");
		}
	}
	
	/**
	 * Valida el dígito verificador de la cédula uruguaya.
	 * Algoritmo oficial: multiplicar los dígitos (excepto el último) por 2,9,8,7,6,3,4
	 * desde la derecha, sumar los productos, calcular módulo 10 y el verificador es 10-módulo (o 0 si módulo es 0)
	 * 
	 * Para cédulas de 7 dígitos: se usan multiplicadores 9,8,7,6,3,4
	 * Para cédulas de 8 dígitos: se usan multiplicadores 2,9,8,7,6,3,4
	 */
	private boolean validarDigitoVerificador(String cedula) {
		int[] multiplicadoresCompletos = {2, 9, 8, 7, 6, 3, 4};
		int longitud = cedula.length();
		int suma = 0;
		
		// Determinar cuántos multiplicadores usar según la longitud
		int cantidadMultiplicadores = longitud - 1; // Todos excepto el último dígito (verificador)
		int offset = multiplicadoresCompletos.length - cantidadMultiplicadores; // Offset para cédulas de 7 dígitos
		
		// Sumar los productos de los dígitos (excepto el último) por los multiplicadores
		for (int i = 0; i < cantidadMultiplicadores; i++) {
			int digito = Character.getNumericValue(cedula.charAt(i));
			suma += digito * multiplicadoresCompletos[i + offset];
		}
		
		// Calcular dígito verificador esperado
		int modulo = suma % 10;
		int verificadorEsperado = (modulo == 0) ? 0 : 10 - modulo;
		
		// Comparar con el último dígito de la cédula
		int verificadorReal = Character.getNumericValue(cedula.charAt(longitud - 1));
		
		return verificadorReal == verificadorEsperado;
	}
}
