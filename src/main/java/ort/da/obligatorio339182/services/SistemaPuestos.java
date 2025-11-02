package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.exceptions.AppException;

@Service
class SistemaPuestos {
	private List<Puesto> puestos;
	private List<Tarifa> tarifas;
	private List<BonificacionAsignada> bonificacionesAsignadas;

	SistemaPuestos() {
		this.puestos = new ArrayList<Puesto>();
		this.tarifas = new ArrayList<Tarifa>();
		this.bonificacionesAsignadas = new ArrayList<BonificacionAsignada>();
	}

	// Agrega un puesto al sistema
	void agregarPuesto(Puesto puesto) {
		puesto.validar();
		this.puestos.add(puesto);
	}

	void agregarTarifa(Tarifa tarifa) {
		tarifa.validar();
		this.tarifas.add(tarifa);
	}

	// Agrega una bonificación asignada al sistema
	void agregarBonificacionAsignada(BonificacionAsignada bonificacionAsignada) throws AppException {
		BonificacionAsignada bonificacionAsignadaExistente = getBonificacionAsignadaEnPuesto(bonificacionAsignada.getPropietario(), bonificacionAsignada.getPuesto());
		if(bonificacionAsignadaExistente != null) {
			throw new AppException("Ya tiene una bonificación asignada para ese puesto");
		}
		bonificacionAsignada.validar();
		this.bonificacionesAsignadas.add(bonificacionAsignada);
	}

	// Obtiene las bonificaciones asignadas a un propietario
	List<BonificacionAsignada> getBonificacionesPorPropietario(Propietario propietario) {
		return bonificacionesAsignadas.stream()
			.filter(ba -> ba.getPropietario().equals(propietario))
			.toList();
	}

	// Obtiene la bonificación que tenía un propietario en un puesto específico
	BonificacionAsignada getBonificacionAsignadaEnPuesto(Propietario propietario, Puesto puesto) {
		return bonificacionesAsignadas.stream()
			.filter(ba -> ba.getPropietario().equals(propietario))
			.filter(ba -> ba.getPuesto().equals(puesto))
			.findFirst()
			.orElse(null);
	}

	Puesto getPuestoPorId(int id) throws AppException {
		Puesto puesto = puestos.stream()
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElse(null);
		if(puesto == null) {
			throw new AppException("Puesto no encontrado");
		}
		return puesto;
	}

	List<Puesto> getTodosPuestos() {
		return puestos;
	}

}
