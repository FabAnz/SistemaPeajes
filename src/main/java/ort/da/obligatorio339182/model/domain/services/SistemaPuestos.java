package ort.da.obligatorio339182.model.domain.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;

@Service
public class SistemaPuestos {
	private static int s_idPuesto;
	private static int s_idTarifa;
	private static int s_idBonificacion;
	private List<Puesto> puestos;
	private List<Tarifa> tarifas;
	private List<BonificacionAsignada> bonificacionesAsignadas;

	public SistemaPuestos() {
		s_idPuesto = 0;
		s_idTarifa = 0;
		s_idBonificacion = 0;
		this.puestos = new ArrayList<Puesto>();
		this.tarifas = new ArrayList<Tarifa>();
		this.bonificacionesAsignadas = new ArrayList<BonificacionAsignada>();
	}

	public void crearPuesto(String nombre, String direccion) {

	}

	public void crearTadrifa(int monto, Categoria categoria) {

	}

	public void crearBonificacion(String nombre, Puesto puesto, Propietario propietario) {

	}

	public List<Bonificacion> getBonificacionesPorUsuario(Cedula cedula) {
		return null;
	}

}
