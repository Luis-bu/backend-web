package co.reales.dw.controllers;

import co.reales.dw.services.EmailService;
import co.reales.dw.services.VerificacionCorreoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificacionCorreoControllerTest {

    private VerificacionCorreoService verificacionService;
    private EmailService emailService;
    private VerificacionCorreoController controller;

    @BeforeEach
    void setUp() {
        verificacionService = Mockito.mock(VerificacionCorreoService.class);
        emailService = Mockito.mock(EmailService.class);
        controller = new VerificacionCorreoController(verificacionService, emailService);
    }

    @Test
    void enviar_ok() {
        String email = "test@gmail.com";
        when(verificacionService.generarToken(email)).thenReturn("abc123");

        ResponseEntity<String> response = controller.enviar(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Correo enviado", response.getBody());
        verify(verificacionService).generarToken(email);
        verify(emailService).enviarCorreo(eq(email), eq("Verifica tu correo"), contains("abc123"));
    }

    @Test
    void confirmar_ok() {
        when(verificacionService.verificar("test@gmail.com", "abc123")).thenReturn(true);

        ResponseEntity<String> response = controller.confirmar("test@gmail.com", "abc123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Correo verificado correctamente", response.getBody());
    }

    @Test
    void confirmar_tokenInvalido() {
        when(verificacionService.verificar("test@gmail.com", "malo")).thenReturn(false);

        ResponseEntity<String> response = controller.confirmar("test@gmail.com", "malo");

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Token inválido", response.getBody());
    }
}