package co.reales.dw.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String nit;

    @Column(nullable = false)
    private String correoContacto;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Proceso> procesos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<RolProceso> roles;
}
