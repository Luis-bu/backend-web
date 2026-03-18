package co.reales.dw.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "arcos")
public class Arco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String etiqueta;

    @ManyToOne
    @JoinColumn(name = "proceso_id", nullable = false)
    private Proceso proceso;

    @ManyToOne
    @JoinColumn(name = "actividad_origen_id")
    private Actividad actividadOrigen;

    @ManyToOne
    @JoinColumn(name = "actividad_destino_id")
    private Actividad actividadDestino;

    @ManyToOne
    @JoinColumn(name = "gateway_origen_id")
    private Gateway gatewayOrigen;

    @ManyToOne
    @JoinColumn(name = "gateway_destino_id")
    private Gateway gatewayDestino;
}