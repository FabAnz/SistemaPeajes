package ort.da.obligatorio339182.model.domain.services;

import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import java.util.ArrayList;
import java.util.List;

@Service
public class SistemaVehiculos {
	private static int s_idCategoria;
	private static int s_idVehiculo;
	private List<Categoria> categorias;
	private List<Vehiculo> vehiculos;

	public SistemaVehiculos() {
		s_idCategoria = 0;
		s_idVehiculo = 0;
		this.categorias = new ArrayList<Categoria>();
		this.vehiculos = new ArrayList<Vehiculo>();
	}

	public void crearCategoria(String nombre) {

	}

	private Categoria getCategoriaPorNombre(String nombre) {
		return null;
	}

	private boolean validarNombreUnicoCategoria(String nombre) {
		return false;
	}

	public void crearVehiculo(Matricula matricula, String modelo, String color, Categoria categoria) {

	}

	private Vehiculo geVehiculoPorMatricula(Matricula matricula) {
		return null;
	}

	private boolean validarMatriculaUnica(Matricula matricula) {
		return false;
	}

	/**
	 * Usuario usuario = fachada.getUsuarioPorCedula(cedula)
	 * return usuario.vehiculos
	 */
	public List<Vehiculo> getVehiculosDelUsuario(Cedula cedula) {
		return null;
	}

}
