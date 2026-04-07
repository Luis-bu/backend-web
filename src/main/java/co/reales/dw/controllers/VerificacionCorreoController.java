package co.reales.dw.controllers;

import co.reales.dw.services.EmailService;
import co.reales.dw.services.VerificacionCorreoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verificacion")
public class VerificacionCorreoController {

    private final VerificacionCorreoService verificacionService;
    private final EmailService emailService;

    public VerificacionCorreoController(VerificacionCorreoService verificacionService,
                                        EmailService emailService) {
        this.verificacionService = verificacionService;
        this.emailService = emailService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviar(@RequestParam String email) {
        String token = verificacionService.generarToken(email);

        String link = "http://localhost:8080/api/verificacion/confirmar?email=" + email + "&token=" + token;

        emailService.enviarCorreo(
                email,
                "Verifica tu correo",
                "Haz clic en este enlace para verificar tu correo:\n" + link
        );

        return ResponseEntity.ok("Correo enviado");
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestParam String email, @RequestParam String token) {
        boolean ok = verificacionService.verificar(email, token);

        if (ok) {
            return ResponseEntity.ok("Correo verificado correctamente");
        }

        return ResponseEntity.badRequest().body("Token inválido");
    }
}