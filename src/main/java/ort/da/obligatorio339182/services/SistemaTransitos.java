package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import org.springframework.context.annotation.Lazy;
import ort.da.obligatorio339182.model.domain.Puesto;

@Service
class SistemaTransitos {
	private List<Transito> transitos;
	private final Fachada fachada;
	SistemaTransitos(@Lazy Fachada fachada) {
		this.transitos = new ArrayList<Transito>();
		this.fachada = fachada;
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
	 * Agrega un tránsito al sistema con la fecha actual
	 */
	void agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo) throws AppException {
		agregarTransito(propietario, puesto, vehiculo, LocalDateTime.now());
	}

	/**
	 * Agrega un tránsito al sistema con una fecha específica
	 * Útil para cargar datos de prueba
	 */
	void agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, LocalDateTime fechaHora) throws AppException {
		BonificacionAsignada bonificacion = fachada.getBonificacionEnPuesto(propietario, puesto);
		boolean esPrimerTransitoDelDia = esPrimerTransitoDelDia(puesto, vehiculo, fechaHora.toLocalDate());

		Transito transito = new Transito(propietario, puesto, vehiculo, bonificacion, esPrimerTransitoDelDia, fechaHora);
		transito.validar();
		this.transitos.add(transito);
	}

	/**
	 * Verifica si es el primer tránsito del día para un vehículo en un puesto
	 * @param puesto El puesto
	 * @param vehiculo El vehículo
	 * @param fecha La fecha a verificar
	 * @return true si es el primer tránsito del día, false en caso contrario
	 */
	boolean esPrimerTransitoDelDia(Puesto puesto, Vehiculo vehiculo, LocalDate fecha) {
		return !transitos.stream()
			.filter(t -> t.getPuesto().equals(puesto))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.anyMatch(t -> t.getFechaHora().toLocalDate().equals(fecha));
	}

	/**
	 * Obtiene todos los tránsitos de un propietario ordenados por fecha descendente
	 * Principio de Experto: SistemaTransitos es el experto en gestionar la colección de tránsitos
	 * @param propietario El propietario
	 * @return Lista de tránsitos ordenada por fechaHora descendente (más reciente primero)
	 */
	List<Transito> getTransitosPorPropietario(Propietario propietario) {
		return transitos.stream()
			.filter(t -> t.getPropietario().equals(propietario))
			.sorted((t1, t2) -> t2.getFechaHora().compareTo(t1.getFechaHora()))
			.toList();
	}

}
