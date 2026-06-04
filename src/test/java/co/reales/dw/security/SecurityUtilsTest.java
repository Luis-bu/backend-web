package co.reales.dw.security;

import co.reales.dw.exceptions.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private void setAuth(Long userId, Long empresaId, String rol) {
        UsuarioPrincipal principal = new UsuarioPrincipal(userId, "user@test.com", empresaId, rol);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }

    @Test
    void getCurrentUser_retornaUsuarioAutenticado() {
        setAuth(1L, 1L, "EDITOR");
        UsuarioPrincipal user = SecurityUtils.getCurrentUser();
        assertEquals(1L, user.getId());
        assertEquals(1L, user.getEmpresaId());
    }

    @Test
    void getCurrentUser_sinAutenticacion_lanzaExcepcion() {
        assertThrows(BadRequestException.class, SecurityUtils::getCurrentUser);
    }

    @Test
    void getCurrentEmpresaId_retornaIdCorrecto() {
        setAuth(1L, 5L, "EDITOR");
        assertEquals(5L, SecurityUtils.getCurrentEmpresaId());
    }

    @Test
    void validarAccesoEmpresa_mismaEmpresaPermitido() {
        setAuth(1L, 1L, "EDITOR");
        assertDoesNotThrow(() -> SecurityUtils.validarAccesoEmpresa(1L));
    }

    @Test
    void validarAccesoEmpresa_otraEmpresaDenegado() {
        setAuth(1L, 1L, "EDITOR");
        assertThrows(BadRequestException.class, () -> SecurityUtils.validarAccesoEmpresa(99L));
    }

    @Test
    void validarAccesoEmpresa_adminPuedeCualquierEmpresa() {
        setAuth(1L, 1L, "ADMINISTRADOR");
        assertDoesNotThrow(() -> SecurityUtils.validarAccesoEmpresa(99L));
    }

    @Test
    void validarAccesoEmpresa_editorNoEsAdmin_bloqueado() {
        setAuth(1L, 2L, "EDITOR");
        assertThrows(BadRequestException.class, () -> SecurityUtils.validarAccesoEmpresa(3L));
    }
}
