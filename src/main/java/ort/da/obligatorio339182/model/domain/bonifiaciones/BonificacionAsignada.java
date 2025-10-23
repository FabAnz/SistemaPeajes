package ort.da.obligatorio339182.model.domain.bonifiaciones;

import java.time.LocalDate;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Puesto;

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
	private LocalDate fechaAsignacion;
	private Propietario propietario;
	private Puesto puesto;
	private Bonificacion bonificacion;

	public BonificacionAsignada(LocalDate fechaAsignacion, Propietario propietario, Puesto puesto, Bonificacion bonificacion) {
		this.id = ++nextId;
		this.fechaAsignacion = fechaAsignacion;
		this.propietario = propietario;
		this.puesto = puesto;
		this.bonificacion = bonificacion;
	}

}
