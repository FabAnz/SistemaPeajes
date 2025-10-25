package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.Vehiculo;

/**
 * DTO para vehículos del propietario
 * Usado en la Historia de Usuario 2.3
 * 
 * IMPORTANTE: Este DTO NO tiene lógica de negocio.
 * Solo transforma datos (Vehiculo + datos calculados → tipos primitivos).
 * Los cálculos se realizan en SistemaTransitos y se pasan como parámetros.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoPropietarioDTO {
    private String matricula;
    private String modelo;
    private String color;
    private int cantidadTransitos;
    private int montoTotalGastado;

    /**
     * Constructor que transforma un Vehiculo y datos ya calculados en un DTO
     * @param vehiculo El vehículo del cual extraer la información
     * @param cantidadTransitos Cantidad de tránsitos (calculado por SistemaTransitos)
     * @param montoTotalGastado Monto total gastado (calculado por SistemaTransitos)
     */
    public VehiculoPropietarioDTO(Vehiculo vehiculo, int cantidadTransitos, int montoTotalGastado) {
        this.matricula = vehiculo.getMatricula().getValor();
        this.modelo = vehiculo.getModelo();
        this.color = vehiculo.getColor();
        this.cantidadTransitos = cantidadTransitos;
        this.montoTotalGastado = montoTotalGastado;
    }
}
