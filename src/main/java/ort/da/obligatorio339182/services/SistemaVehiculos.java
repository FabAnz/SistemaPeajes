package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import java.util.ArrayList;
import java.util.List;

@Service
class SistemaVehiculos {
	private List<Categoria> categorias;
	private List<Vehiculo> vehiculos;

	SistemaVehiculos() {
		this.categorias = new ArrayList<Categoria>();
		this.vehiculos = new ArrayList<Vehiculo>();
	}

	void crearCategoria(String nombre) {

	}

	private Categoria getCategoriaPorNombre(String nombre) {
		return null;
	}

	private boolean validarNombreUnicoCategoria(String nombre) {
		return false;
	}

	void agregarVehiculo(Vehiculo vehiculo) throws AppException {
		vehiculo.validar();
		this.vehiculos.add(vehiculo);
	}

	private Vehiculo getVehiculoPorMatricula(Matricula matricula) throws AppException {
		for (Vehiculo vehiculo : vehiculos) {
			if (vehiculo.getMatricula().equals(matricula)) {
				return vehiculo;
			}
		}
		throw new AppException("Vehículo no encontrado");
	}

	private boolean validarMatriculaUnica(Matricula matricula) {
		return false;
	}

	/**
	 * Usuario usuario = fachada.getUsuarioPorCedula(cedula)
	 * return usuario.vehiculos
	 */
	List<Vehiculo> getVehiculosDelUsuario(Cedula cedula) {
		return null;
	}

}
