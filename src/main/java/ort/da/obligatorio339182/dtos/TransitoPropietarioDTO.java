package ort.da.obligatorio339182.dtos;

import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public class TransitoPropietarioDTO {
	
	private String puesto;
	private String matricula;
	private String categoria;
	private int montoTarifa;
	private int montoBonificacion;
	private String bonificacionAplicada;
	private int montoPagado;
	private String fecha;
	private String hora;

	/**
	 * Constructor que calcula toda la información del tránsito
	 * @param transito El tránsito
	 * @param bonificacionAsignada La bonificación que tenía en ese momento (puede ser null)
	 */
	public TransitoPropietarioDTO(Transito transito, BonificacionAsignada bonificacionAsignada) {
		this.puesto = transito.getPuesto().getNombre();
		this.matricula = transito.getVehiculo().getMatricula().getValor();
		this.categoria = transito.getVehiculo().getCategoria().getNombre();
		this.montoTarifa = transito.getTarifaOriginal();
		this.montoBonificacion = transito.getMontoBonificacion();
		this.bonificacionAplicada = montoBonificacion > 0
			? bonificacionAsignada.getBonificacion().getNombre() 
			: "Sin bonificación";
		this.montoPagado = transito.getCobro();
		this.fecha = transito.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.hora = transito.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm"));
	}

}

