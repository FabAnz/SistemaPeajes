package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;

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
	private BonificacionAsignada bonificacion;
	private boolean esPrimerTransitoDelDia;

	public Transito(
			Propietario propietario,
			Puesto puesto,
			Vehiculo vehiculo,
			BonificacionAsignada bonificacion,
			boolean esPrimerTransitoDelDia)
			throws AppException {
		this.id = ++nextId;
		this.fechaHora = LocalDateTime.now();
		this.propietario = propietario;
		this.puesto = puesto;
		this.vehiculo = vehiculo;
		this.bonificacion = bonificacion;
		this.calcularCobro();
		this.esPrimerTransitoDelDia = esPrimerTransitoDelDia;
	}

	public void validar() throws AppException {
		if (cobro < 0) {
			throw new AppException("El cobro debe ser mayor o igual a 0");
		}
		if (fechaHora == null) {
			throw new AppException("La fecha no puede ser null");
		}
		if (propietario == null) {
			throw new AppException("El propietario no puede ser null");
		}
		if (puesto == null) {
			throw new AppException("El puesto no puede ser null");
		}
		if (vehiculo == null) {
			throw new AppException("El vehiculo no puede ser null");
		}
	}

	public void calcularCobro() throws AppException {
		int tarifaOriginal = getTarifaOriginal();
		int montoBonificacion = getMontoBonificacion();
		this.cobro = tarifaOriginal - montoBonificacion;
	}

	/**
	 * Calcula la tarifa original antes de aplicar bonificaciones
	 * Principio de Experto: Transito conoce su puesto y vehículo,
	 * delega al Puesto (experto en tarifas) el cálculo
	 * 
	 * @return Monto de la tarifa original
	 */
	public int getTarifaOriginal() {
		return puesto.getTarifaPorCategoria(vehiculo.getCategoria());
	}

	/**
	 * Calcula el monto de bonificación aplicada en este tránsito
	 * Principio de Experto: Transito es experto en calcular la diferencia
	 * entre su tarifa original y el cobro final
	 * 
	 * @return Monto del descuento por bonificación
	 */
	public int getMontoBonificacion() {
		if(bonificacion == null || !propietario.puedeRecibirBonificaciones()) {
			return 0;
		}
		int porcentajeABonificacion = bonificacion.getPorcentajeBonificacion(this);
		return getTarifaOriginal() * porcentajeABonificacion / 100;
	}

	/**
	 * Obtiene la fecha en formato dd/MM/yyyy
	 * 
	 * @return Fecha formateada
	 */
	public String getFechaFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return fechaHora.format(formatter);
	}

	/**
	 * Obtiene la hora en formato HH:mm
	 * 
	 * @return Hora formateada
	 */
	public String getHoraFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return fechaHora.format(formatter);
	}

}
