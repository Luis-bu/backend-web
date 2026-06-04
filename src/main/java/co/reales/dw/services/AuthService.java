package co.reales.dw.services;

import co.reales.dw.dtos.LoginRequestDTO;
import co.reales.dw.dtos.LoginResponseDTO;
import co.reales.dw.entities.Usuario;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.UsuarioRepository;
import co.reales.dw.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getEmpresa().getId(),
                usuario.getRol().name()
        );

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol().name(),
                usuario.getEmpresa().getId()
        );
    }
}
