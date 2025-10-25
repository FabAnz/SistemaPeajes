package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import ort.da.obligatorio339182.exceptions.AppException;

@Data
@NoArgsConstructor
public class Transito {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private int cobro;
	private LocalDateTime fechaHora;
	private Propietario propietario;
	private Puesto puesto;
	private Vehiculo vehiculo;

	public Transito(int cobro, Propietario propietario, Puesto puesto, Vehiculo vehiculo) {
		this.id = ++nextId;
		this.cobro = cobro;
		this.fechaHora = LocalDateTime.now();
		this.propietario = propietario;
		this.puesto = puesto;
		this.vehiculo = vehiculo;
	}

	public void validar() throws AppException {
		if(cobro <= 0) {
			throw new AppException("El cobro debe ser mayor a 0");
		}
		if(fechaHora == null) {
			throw new AppException("La fecha no puede ser null");
		}
		if(propietario == null) {
			throw new AppException("El propietario no puede ser null");
		}
		if(puesto == null) {
			throw new AppException("El puesto no puede ser null");
		}
		if(vehiculo == null) {
			throw new AppException("El vehiculo no puede ser null");
		}
	}

	/**
	 * Calcula la tarifa original antes de aplicar bonificaciones
	 * Principio de Experto: Transito conoce su puesto y vehículo, 
	 * delega al Puesto (experto en tarifas) el cálculo
	 * @return Monto de la tarifa original
	 */
	public int getTarifaOriginal() {
		return puesto.getTarifaPorCategoria(vehiculo.getCategoria());
	}

	/**
	 * Calcula el monto de bonificación aplicada en este tránsito
	 * Principio de Experto: Transito es experto en calcular la diferencia
	 * entre su tarifa original y el cobro final
	 * @return Monto del descuento por bonificación
	 */
	public int getMontoBonificacion() {
		return getTarifaOriginal() - cobro;
	}

	/**
	 * Obtiene la fecha en formato dd/MM/yyyy
	 * @return Fecha formateada
	 */
	public String getFechaFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return fechaHora.format(formatter);
	}

	/**
	 * Obtiene la hora en formato HH:mm
	 * @return Hora formateada
	 */
	public String getHoraFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return fechaHora.format(formatter);
	}

}
