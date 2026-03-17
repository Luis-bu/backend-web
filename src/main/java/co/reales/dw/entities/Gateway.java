package co.reales.dw.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "gateways")
public class Gateway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoGateway tipo;

    @ManyToOne
    @JoinColumn(name = "proceso_id", nullable = false)
    private Proceso proceso;

    public enum TipoGateway {
        EXCLUSIVO, PARALELO, INCLUSIVO
    }
}