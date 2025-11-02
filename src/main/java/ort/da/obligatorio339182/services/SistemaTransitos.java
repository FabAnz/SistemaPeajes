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
import ort.da.obligatorio339182.model.domain.Notificacion;

@Service
class SistemaTransitos {
	private List<Transito> transitos;
	private final Fachada fachada;
	SistemaTransitos(@Lazy Fachada fachada) {
		this.transitos = new ArrayList<Transito>();
		this.fachada = fachada;
	}


	int cantidadTransitosPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return (int) transitos.stream()
			.filter(t -> t.getPropietario().equals(propietario))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.count();
	}


	int montoTotalPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return transitos.stream()
			.filter(t -> t.getPropietario().equals(propietario))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.mapToInt(Transito::getCobro)
			.sum();
	}


	Transito agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo) throws AppException {
		return agregarTransito(propietario, puesto, vehiculo, LocalDateTime.now());
	}


	Transito agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, LocalDateTime fechaHora) throws AppException {
		BonificacionAsignada bonificacion = fachada.getBonificacionEnPuesto(propietario, puesto);
		boolean esPrimerTransitoDelDia = esPrimerTransitoDelDia(puesto, vehiculo, fechaHora.toLocalDate());

		Transito transito = new Transito(propietario, puesto, vehiculo, bonificacion, esPrimerTransitoDelDia, fechaHora);
		transito.validar();
		this.transitos.add(transito);
		propietario.agregarTransito(transito);

		//Crear notificaciones
		if (propietario.recibeNotificaciones()) {
			String mensajeTransito = "Pasaste por el puesto " + puesto.getNombre() + " con el vehículo " + vehiculo.getMatricula().getValor();
			Notificacion notificacion = new Notificacion(mensajeTransito, fechaHora);
			propietario.agregarNotificacion(notificacion);
		}
		
		return transito;
	}

	// Verifica si es el primer tránsito del día para un vehículo en un puesto

	boolean esPrimerTransitoDelDia(Puesto puesto, Vehiculo vehiculo, LocalDate fecha) {
		return !transitos.stream()
			.filter(t -> t.getPuesto().equals(puesto))
			.filter(t -> t.getVehiculo().equals(vehiculo))
			.anyMatch(t -> t.getFechaHora().toLocalDate().equals(fecha));
	}

}
