package co.reales.dw.config;

import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigration {

    private static final Logger log = LoggerFactory.getLogger(PasswordMigration.class);

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public PasswordMigration(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostConstruct
    public void migrarContrasenasPlanas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        int migrados = 0;
        for (Usuario usuario : usuarios) {
            String contrasena = usuario.getContrasena();
            if (contrasena != null && !contrasena.startsWith("$2a$") && !contrasena.startsWith("$2b$")) {
                usuario.setContrasena(encoder.encode(contrasena));
                usuarioRepository.save(usuario);
                migrados++;
            }
        }
        if (migrados > 0) {
            log.info("{} contraseñas migradas a BCrypt.", migrados);
        }
    }
}
