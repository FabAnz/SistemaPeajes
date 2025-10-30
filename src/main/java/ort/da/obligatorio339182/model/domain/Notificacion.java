package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.time.LocalDateTime;
import ort.da.obligatorio339182.exceptions.AppException;

@Data
@NoArgsConstructor
public class Notificacion {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String mensaje;
	private LocalDateTime fechaHora;

	public Notificacion(String mensaje) {
		this.id = ++nextId;
		this.mensaje = mensaje;
		this.fechaHora = LocalDateTime.now();
	}

	public Notificacion(String mensaje, LocalDateTime fechaHora) {
		this.id = ++nextId;
		this.mensaje = mensaje;
		this.fechaHora = fechaHora;
	}

	public void validar() throws AppException {
		if(mensaje == null || mensaje.isBlank()) {
			throw new AppException("El mensaje no puede ser null o vacío");
		}
		if(fechaHora == null) {
			throw new AppException("La fecha y hora no pueden ser null");
		}
	}

}
