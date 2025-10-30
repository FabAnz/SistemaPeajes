package ort.da.obligatorio339182.model.domain.bonifiaciones;

import java.time.LocalDateTime;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.Transito;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
public class BonificacionAsignada {
	private static int nextId = 0;
	
	@Setter(AccessLevel.PRIVATE)
	private int id;
	private LocalDateTime fechaAsignacion;
	private Propietario propietario;
	private Puesto puesto;
	private Bonificacion bonificacion;

	public BonificacionAsignada(Propietario propietario, Puesto puesto, Bonificacion bonificacion) {
		this.id = ++nextId;
		this.fechaAsignacion = LocalDateTime.now();
		this.propietario = propietario;
		this.puesto = puesto;
		this.bonificacion = bonificacion;
	}

	public void validar() throws AppException {
		if (this.id <= 0) {
			throw new AppException("El id debe ser mayor a 0");
		}
		if (this.fechaAsignacion == null) {
			throw new AppException("La fecha de asignación no puede ser null");
		}
		if (this.propietario == null) {
			throw new AppException("El propietario no puede ser null");
		}
		if (this.puesto == null) {
			throw new AppException("El puesto no puede ser null");
		}
		if (this.bonificacion == null) {
			throw new AppException("La bonificación no puede ser null");
		}
		// Validar que el propietario pueda recibir bonificaciones (no puede estar deshabilitado)
		try {
			this.propietario.getEstado().validarAccesoAlSistema();
		} catch (AppException e) {
			throw new AppException("El propietario no puede recibir bonificaciones");
		}
	}

	public int getPorcentajeBonificacion(Transito transito) {
		return this.bonificacion.getBonificacion(transito);
	}

}
