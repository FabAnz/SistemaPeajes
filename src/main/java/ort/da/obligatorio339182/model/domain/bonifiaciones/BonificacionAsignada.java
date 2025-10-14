package ort.da.obligatorio339182.model.domain.bonifiaciones;

import java.time.LocalDate;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.Puesto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionAsignada {
	private int id;
	private LocalDate fechaAsignacion;
	private Propietario propietario;
	private Puesto puesto;
	private Bonificacion bonificacion;

}
