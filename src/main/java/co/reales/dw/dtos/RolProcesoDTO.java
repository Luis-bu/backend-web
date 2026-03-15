package co.reales.dw.dtos;

import lombok.Data;

@Data
public class RolProcesoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long empresaId;
}