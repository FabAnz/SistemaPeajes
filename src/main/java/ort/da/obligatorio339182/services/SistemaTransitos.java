package ort.da.obligatorio339182.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.valueObjects.Matricula;

@Service
class SistemaTransitos {
	private List<Transito> transitos;

	SistemaTransitos() {
		this.transitos = new ArrayList<Transito>();
	}

	/**
	 * Usuario usuario = fachada.getUsuarioPorCedula(cedula)
	 * List<Transito> transitos = new List><Transito>();
	 * 
	 * add transitos con misma matricula
	 * return transitos
	 */
	private List<Transito> getTransitosPorCedulaYMatricula(Cedula cedula, Matricula matricula) {
		return null;
	}

	/**
	 * List<Transitos> transitos = getTransitosPorCedulaYMatricula(cedula, matricula)
	 * 
	 * return transitos.size();
	 */
	int cantidadTransitosPorCedulaYMatricula(Cedula cedula, Matricula matricula) {
		return 0;
	}

	/**
	 * List<Transitos> transitos = getTransitosPorCedulaYMatricula(cedula, matricula)
	 * 
	 * int total=0;
	 * 
	 * sumar los totales de cobro de transitos
	 * 
	 * return total;
	 */
	int gastoEnTransitos(Cedula cedula, Matricula matricula) {
		return 0;
	}

}
