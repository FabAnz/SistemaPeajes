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
		this(propietario, puesto, vehiculo, bonificacion, esPrimerTransitoDelDia, LocalDateTime.now());
	}

	// Constructor que permite especificar la fecha del tránsito
	// Útil para cargar datos de prueba con fechas específicas
	public Transito(
			Propietario propietario,
			Puesto puesto,
			Vehiculo vehiculo,
			BonificacionAsignada bonificacion,
			boolean esPrimerTransitoDelDia,
			LocalDateTime fechaHora)
			throws AppException {
		this.id = ++nextId;
		this.fechaHora = fechaHora;
		this.propietario = propietario;
		this.puesto = puesto;
		this.vehiculo = vehiculo;
		this.bonificacion = bonificacion;
		this.esPrimerTransitoDelDia = esPrimerTransitoDelDia;
		this.calcularCobro();
	}

	public void validar() throws AppException {
		if(!propietario.puedeRealizarTransitos()) {
			throw new AppException("El propietario no puede realizar tránsitos");
		}
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
		int pagoTotal = tarifaOriginal - montoBonificacion;

		this.propietario.restarSaldo(pagoTotal);
		this.cobro = pagoTotal;
	}

	// Calcula la tarifa original antes de aplicar bonificaciones
	public int getTarifaOriginal() {
		return puesto.getTarifaPorCategoria(vehiculo.getCategoria());
	}

	// Calcula el monto de bonificación aplicada en este tránsito
	public int getMontoBonificacion() {
		if(bonificacion == null || !propietario.aplicanBonificaciones()) {
			return 0;
		}
		int porcentajeABonificacion = bonificacion.getPorcentajeBonificacion(this);
		return getTarifaOriginal() * porcentajeABonificacion / 100;
	}

}
