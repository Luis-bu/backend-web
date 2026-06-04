package co.reales.dw.config;

import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordMigrationTest {

    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private PasswordMigration passwordMigration;

    @Test
    void migrarContrasenasPlanas_migraContrasenaPlana() {
        Usuario usuario = new Usuario();
        usuario.setContrasena("contrasena123");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        passwordMigration.migrarContrasenasPlanas();

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        String contrasenaGuardada = captor.getValue().getContrasena();
        assertTrue(contrasenaGuardada.startsWith("$2a$") || contrasenaGuardada.startsWith("$2b$"),
                "La contraseña debe haber sido hasheada con BCrypt");
    }

    @Test
    void migrarContrasenasPlanas_noMigraContrasenaYaHasheada() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuario = new Usuario();
        usuario.setContrasena(encoder.encode("yaHasheada"));

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        passwordMigration.migrarContrasenasPlanas();

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void migrarContrasenasPlanas_sinUsuarios_noHaceNada() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        passwordMigration.migrarContrasenasPlanas();

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void migrarContrasenasPlanas_contrasenaNull_noMigra() {
        Usuario usuario = new Usuario();
        usuario.setContrasena(null);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        passwordMigration.migrarContrasenasPlanas();

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void migrarContrasenasPlanas_variosUsuariosMixtos_soloMigraPlanas() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario u1 = new Usuario(); u1.setContrasena("plana123");
        Usuario u2 = new Usuario(); u2.setContrasena(encoder.encode("yaHasheada"));
        Usuario u3 = new Usuario(); u3.setContrasena("otraPlana");

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2, u3));

        passwordMigration.migrarContrasenasPlanas();

        verify(usuarioRepository, times(2)).save(any());
    }
}
