package co.reales.dw.controllers;

import co.reales.dw.dtos.LoginRequestDTO;
import co.reales.dw.dtos.LoginResponseDTO;
import co.reales.dw.dtos.UsuarioRequestDTO;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import co.reales.dw.services.AuthService;
import co.reales.dw.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(AuthService authService, UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody UsuarioRequestDTO dto) {
        usuarioService.crearUsuario(dto, false);
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setCorreo(dto.getCorreo());
        loginRequest.setContrasena(dto.getContrasena());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // Endpoint temporal: hashea las contraseñas existentes en texto plano
    @PostMapping("/fix-password")
    public ResponseEntity<String> fixPassword(@RequestBody LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setContrasena(encoder.encode(request.getContrasena()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
