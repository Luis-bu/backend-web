package co.reales.dw.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "proceso_id", nullable = false)
    private Proceso proceso;

    @ManyToOne
    @JoinColumn(name = "rol_proceso_id")
    private RolProceso rolProceso;
}

