package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
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

	void agregarCategoria(Categoria categoria) throws AppException {
		categoria.validar();
		this.categorias.add(categoria);
	}

	void agregarVehiculoConPropietario(Vehiculo vehiculo, Propietario propietario) throws AppException {
		vehiculo.validar();
		vehiculo.setPropietario(propietario);
		propietario.agregarVehiculo(vehiculo);
		this.agregarVehiculo(vehiculo);
	}

	void agregarVehiculo(Vehiculo vehiculo) throws AppException {
		if (vehiculo.getPropietario() == null) {
			vehiculo.validar();
		}
		Matricula matricula = vehiculo.getMatricula();
		if (validarMatriculaUnica(matricula)) {
			throw new AppException("La matrícula ya está en uso");
		}
		this.vehiculos.add(vehiculo);
	}

	Vehiculo getVehiculoPorMatricula(Matricula matricula) throws AppException {
		Vehiculo vehiculo = vehiculos.stream()
				.filter(v -> v.getMatricula().equals(matricula))
				.findFirst()
				.orElse(null);
		if(vehiculo == null) {
			throw new AppException("No existe el vehículo");
		}
		return vehiculo;
	}

	boolean validarMatriculaUnica(Matricula matricula) {
		return vehiculos.stream()
				.anyMatch(v -> v.getMatricula().equals(matricula));
	}

	/**
	 * Usuario usuario = fachada.getUsuarioPorCedula(cedula)
	 * return usuario.vehiculos
	 */
	List<Vehiculo> getVehiculosDelUsuario(Cedula cedula) {
		return null;
	}

}
