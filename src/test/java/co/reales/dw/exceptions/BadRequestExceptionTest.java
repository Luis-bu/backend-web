package co.reales.dw.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadRequestExceptionTest {

    // test del constructor de BadRequestException
    @Test
    void testBadRequestExceptionConstructor() {
        String mensaje = "Error de prueba";
        BadRequestException exception = new BadRequestException(mensaje);
        assertEquals(mensaje, exception.getMessage());
    }
}