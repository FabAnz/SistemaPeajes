package ort.da.obligatorio339182.dtos;

import lombok.Getter;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

/**
 * DTO para el resultado de emulación de tránsito
 */
@Getter
public class ResultadoEmulacionTransitoDTO {
	private String nombrePropietario;
	private String estadoPropietario;
	private String categoriaVehiculo;
	private String bonificacion;
	private int costoTransito;
	private int saldoLuegoTransito;

	public ResultadoEmulacionTransitoDTO(Transito transito, Propietario propietario) {
		this.nombrePropietario = propietario.getNombreCompleto();
		this.estadoPropietario = propietario.getEstado().getNombre();
		this.categoriaVehiculo = transito.getVehiculo().getCategoria().getNombre();
		this.bonificacion = propietario.aplicanBonificaciones() ? transito.getBonificacion().getBonificacion().getNombre() : "No aplica";
		this.costoTransito = transito.getCobro();
		this.saldoLuegoTransito = propietario.getSaldo();
	}
	
}

