package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.exceptions.AppException;

@Service
class SistemaTransitos {
	private List<Transito> transitos;

	SistemaTransitos() {
		this.transitos = new ArrayList<Transito>();
	}

	/**
	 * Cuenta la cantidad de tránsitos realizados por un propietario en un vehículo específico
	 * @param propietario El propietario
	 * @param vehiculo El vehículo
	 * @return Cantidad de tránsitos
	 */
	int cantidadTransitosPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return (int) transitos.stream()
			.filter(t -> t.getPropietario().equals(propietario))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.count();
	}

	/**
	 * Calcula el monto total gastado por un propietario en un vehículo específico
	 * @param propietario El propietario
	 * @param vehiculo El vehículo
	 * @return Monto total gastado
	 */
	int montoTotalPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return transitos.stream()
			.filter(t -> t.getPropietario().equals(propietario))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.mapToInt(Transito::getCobro)
			.sum();
	}

	/**
	 * Agrega un tránsito al sistema
	 * @param transito El tránsito a agregar
	 */
	void agregarTransito(Transito transito) throws AppException {
		transito.validar();
		this.transitos.add(transito);
	}

}
