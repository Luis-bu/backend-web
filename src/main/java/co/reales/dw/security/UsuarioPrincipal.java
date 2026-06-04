package co.reales.dw.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    private final Long id;
    private final String correo;
    private final Long empresaId;
    private final String rol;

    public UsuarioPrincipal(Long id, String correo, Long empresaId, String rol) {
        this.id = id;
        this.correo = correo;
        this.empresaId = empresaId;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public Long getEmpresaId() { return empresaId; }
    public String getRol() { return rol; }

    @Override public String getUsername() { return correo; }
    @Override public String getPassword() { return null; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }
}
