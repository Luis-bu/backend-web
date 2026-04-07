package co.reales.dw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerificacionCorreoServiceTest {

    private VerificacionCorreoService service;

    @BeforeEach
    void setUp() {
        service = new VerificacionCorreoService();
    }

    @Test
    void generarToken_ok() {
        String token = service.generarToken("test@gmail.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertFalse(service.estaVerificado("test@gmail.com"));
    }

    @Test
    void verificar_ok() {
        String email = "test@gmail.com";
        String token = service.generarToken(email);

        boolean resultado = service.verificar(email, token);

        assertTrue(resultado);
        assertTrue(service.estaVerificado(email));
    }

    @Test
    void verificar_tokenIncorrecto() {
        String email = "test@gmail.com";
        service.generarToken(email);

        boolean resultado = service.verificar(email, "token-malo");

        assertFalse(resultado);
        assertFalse(service.estaVerificado(email));
    }
}