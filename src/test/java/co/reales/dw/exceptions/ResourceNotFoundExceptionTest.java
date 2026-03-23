package co.reales.dw.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    //verificar que el constructor de ResourceNotFoundException asigna correctamente el mensaje
    @Test
    void testConstructor() {
        String mensaje = "Recurso no encontrado";
        ResourceNotFoundException ex = new ResourceNotFoundException(mensaje);
        assertEquals(mensaje, ex.getMessage());
    }
}