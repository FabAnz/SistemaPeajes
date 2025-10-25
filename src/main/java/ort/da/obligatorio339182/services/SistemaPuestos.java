package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.dtos.bonifiaciones.BonificacionAsignada;
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

	/**
	 * Agrega un puesto al sistema
	 * @param puesto El puesto a agregar
	 */
	void agregarPuesto(Puesto puesto) {
		puesto.validar();
		this.puestos.add(puesto);
	}

	void agregarTarifa(Tarifa tarifa) {
		tarifa.validar();
		this.tarifas.add(tarifa);
	}

	/**
	 * Agrega una bonificación asignada al sistema
	 * @param bonificacionAsignada La bonificación asignada a agregar
	 */
	void agregarBonificacionAsignada(BonificacionAsignada bonificacionAsignada) throws AppException {
		bonificacionAsignada.validar();
		this.bonificacionesAsignadas.add(bonificacionAsignada);
	}

	/**
	 * Obtiene las bonificaciones asignadas a un propietario
	 * @param propietario El propietario
	 * @return Lista de bonificaciones asignadas al propietario
	 */
	List<BonificacionAsignada> getBonificacionesPorPropietario(Propietario propietario) {
		return bonificacionesAsignadas.stream()
			.filter(ba -> ba.getPropietario().equals(propietario))
			.toList();
	}

	/**
	 * Obtiene la bonificación que tenía un propietario en un puesto específico en una fecha dada
	 * Principio de Experto: SistemaPuestos es el experto en gestionar bonificaciones asignadas
	 * @param propietario El propietario
	 * @param puesto El puesto
	 * @param fechaTransito La fecha/hora del tránsito
	 * @return La bonificación asignada si existía en ese momento, null en caso contrario
	 */
	BonificacionAsignada getBonificacionAsignadaEnPuesto(Propietario propietario, Puesto puesto, LocalDateTime fechaTransito) {
		return bonificacionesAsignadas.stream()
			.filter(ba -> ba.getPropietario().equals(propietario))
			.filter(ba -> ba.getPuesto().equals(puesto))
			.filter(ba -> !ba.getFechaAsignacion().isAfter(fechaTransito.toLocalDate()))
			.findFirst()
			.orElse(null);
	}

}
