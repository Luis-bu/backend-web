package co.reales.dw.dtos;

import lombok.Data;

@Data
public class ActividadDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Long procesoId;
    private Long rolProcesoId;
}