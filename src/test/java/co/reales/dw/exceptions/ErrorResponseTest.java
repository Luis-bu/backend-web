package co.reales.dw.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    // test del constructor de ErrorResponse
    @Test
    void testErrorResponseConstructor() {
        int status = 400;
        String mensaje = "Bad Request";
        ErrorResponse errorResponse = new ErrorResponse(status, mensaje);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(mensaje, errorResponse.getMensaje());
        assertNotNull(errorResponse.getTimestamp());
    }
}