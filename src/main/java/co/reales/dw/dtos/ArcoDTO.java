package co.reales.dw.dtos;

import lombok.Data;

@Data
public class ArcoDTO {
    private Long id;
    private String etiqueta;
    private Long procesoId;
    private Long actividadOrigenId;
    private Long actividadDestinoId;
    private Long gatewayOrigenId;
    private Long gatewayDestinoId;
}