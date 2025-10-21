package ort.da.obligatorio339182.services;

import ort.da.obligatorio339182.model.valueObjects.Cedula;
import java.util.List;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import jakarta.servlet.http.HttpSession;

@Service
public class Fachada {

	private final SistemaUsuarios su;
	private final SistemaPuestos sp;
	private final SistemaVehiculos sv;
	private final SistemaTransitos st;
	private final SistemaAutorizacion sa;

	public Fachada(SistemaUsuarios su, SistemaPuestos sp, SistemaVehiculos sv, SistemaTransitos st, SistemaAutorizacion sa) {
		this.su = su;
		this.sp = sp;
		this.sv = sv;
		this.st = st;
		this.sa = sa;
	}


	public Usuario login(String cedula, String contrasenia) throws AppException {
		return su.login(cedula, contrasenia);
	}

	public void agregarUsuario(Usuario usuario) throws AppException{
		su.agregarUsuario(usuario);
	}

	public Usuario validarSesionYPermiso(HttpSession session, Permiso permisoRequerido) throws UnauthorizedException {
		return sa.validarSesionYPermiso(session, permisoRequerido);
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

	public Usuario getUsuarioPorCedula(String cedula) {
		return su.getUsuarioPorCedula(cedula);
	}

	/**
	 * Obtiene un usuario por su ID
	 * @param id El ID del usuario
	 * @return El usuario si existe, null en caso contrario
	 */
	public Usuario getUsuarioPorId(int id) {
		return su.getUsuarioPorId(id);
	}

	public int cantidadTransitosPorCedulaYMatricula(Cedula cedula, Matricula matricula) {
		return 0;
	}

	public int gastoEnTransitos(Cedula cedula, Matricula matricula) {
		return 0;
	}

}
