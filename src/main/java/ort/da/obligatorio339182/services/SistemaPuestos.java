package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.valueObjects.Cedula;

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

	void agregarPuesto(Puesto puesto) {
		this.puestos.add(puesto);
	}

	void agregarTarifa(Tarifa tarifa) {
		this.tarifas.add(tarifa);
	}

	void agregarBonificacionAsignada(BonificacionAsignada bonificacionAsignada) {
		this.bonificacionesAsignadas.add(bonificacionAsignada);
	}

	List<BonificacionAsignada> getBonificacionesAsignadasPorUsuario(Cedula cedula) {
		return bonificacionesAsignadas.stream()
			.filter(ba -> ba.getPropietario().getCedula().equals(cedula))
			.toList();
	}

}
