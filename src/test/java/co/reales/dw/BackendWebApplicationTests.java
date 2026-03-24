package co.reales.dw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.*;

class BackendWebApplicationTest {

	// Método main test
    @Test
    void main_ok() {
        assertDoesNotThrow(() -> BackendWebApplication.main(new String[]{}));
    }

    @Test
    void configure_ok() {
        BackendWebApplication app = new BackendWebApplication();
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        SpringApplicationBuilder result = app.configure(builder);
        assertNotNull(result);
    }
}