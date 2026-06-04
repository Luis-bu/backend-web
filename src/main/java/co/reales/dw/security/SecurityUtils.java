package co.reales.dw.security;

import co.reales.dw.exceptions.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static UsuarioPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UsuarioPrincipal principal) {
            return principal;
        }
        throw new BadRequestException("Usuario no autenticado");
    }

    public static Long getCurrentEmpresaId() {
        return getCurrentUser().getEmpresaId();
    }

    public static void validarAccesoEmpresa(Long empresaId) {
        UsuarioPrincipal user = getCurrentUser();
        if ("ADMINISTRADOR".equals(user.getRol())) {
            return;
        }
        if (!user.getEmpresaId().equals(empresaId)) {
            throw new BadRequestException("Acceso denegado: no pertenece a esta empresa");
        }
    }
}
