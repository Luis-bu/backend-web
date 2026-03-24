package co.reales.dw.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    // Test para manejar ResourceNotFoundException
    @Test
    void testHandleNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");
        var response = handler.handleNotFound(ex);

        assertNotNull(response, "El ResponseEntity no debe ser nulo");
        assertNotNull(response.getBody(), "El body no debe ser nulo");
        assertEquals(404, response.getBody().getStatus(), "El status debe ser 404");
        assertEquals("Recurso no encontrado", response.getBody().getMensaje(), "El mensaje debe coincidir");
        assertNotNull(response.getBody().getTimestamp(), "El timestamp no debe ser nulo");
    }

    // Test para manejar BadRequestException
    @Test
    void testHandleBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        BadRequestException ex = new BadRequestException("Solicitud incorrecta");

        var response = handler.handleBadRequest(ex);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Solicitud incorrecta", response.getBody().getMensaje());
        assertNotNull(response.getBody().getTimestamp());
    }

    // Test para manejar excepciones generales
    @Test
    void testHandleGeneral() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Error general");

        var response = handler.handleGeneral(ex);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertNotNull(response.getBody().getTimestamp());
    }
}