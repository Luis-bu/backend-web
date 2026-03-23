package co.reales.dw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.*;

class ServletInitializerTest {

    // Configuración del servlet
    @Test
    void configure_ok() {
        ServletInitializer initializer = new ServletInitializer();

        SpringApplicationBuilder builder = new SpringApplicationBuilder();

        SpringApplicationBuilder result = initializer.configure(builder);

        assertNotNull(result);
    }
}