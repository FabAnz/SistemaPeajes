package ort.da.obligatorio339182.model;

import ort.da.obligatorio339182.model.domain.services.SistemaUsuarios;
import ort.da.obligatorio339182.model.domain.services.SistemaPuestos;
import ort.da.obligatorio339182.model.domain.services.SistemaVehiculos;
import ort.da.obligatorio339182.model.domain.services.SistemaTransitos;
import ort.da.obligatorio339182.dtos.LoginDTO;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import java.util.List;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import org.springframework.stereotype.Service;

@Service
public class Fachada {

	private final SistemaUsuarios su;
	private final SistemaPuestos sp;
	private final SistemaVehiculos sv;
	private final SistemaTransitos st;

	public Fachada(SistemaUsuarios su, SistemaPuestos sp, SistemaVehiculos sv, SistemaTransitos st) {
		this.su = su;
		this.sp = sp;
		this.sv = sv;
		this.st = st;
	}

	public void iniciarSistema() {

	}

	public void login(LoginDTO usuario) {

	}

	/**
	 * sp.getBonificacionesPorUsuario(cedula)
	 */
	public List<Bonificacion> getBonificacionesPorUsuario(Cedula cedula) {
		return null;
	}

	public List<Vehiculo> getVehiculosDelUsuario(Cedula cedula) {
		return null;
	}

	public Usuario getUsuarioPorCedula(Cedula cedula) {
		return null;
	}

	public int cantidadTransitosPorCedulaYMatricula(Cedula cedula, Matricula matricula) {
		return 0;
	}

	public int gastoEnTransitos(Cedula cedula, Matricula matricula) {
		return 0;
	}

}
