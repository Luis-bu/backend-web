package co.reales.dw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "ProcessFlowSecretKeyForJWTAuthenticationMinimum256BitsLong2024");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void testGenerarTokenContieneCorreo() {
        String token = jwtUtil.generateToken(1L, "user@test.com", 10L, "ADMINISTRADOR");
        assertNotNull(token);
        assertEquals("user@test.com", jwtUtil.getCorreoFromToken(token));
    }

    @Test
    void testTokenValido() {
        String token = jwtUtil.generateToken(1L, "user@test.com", 10L, "EDITOR");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testTokenInvalido() {
        assertFalse(jwtUtil.validateToken("token.invalido.xyz"));
    }

    @Test
    void testExtraeEmpresaId() {
        String token = jwtUtil.generateToken(1L, "user@test.com", 42L, "ADMINISTRADOR");
        assertEquals(42L, jwtUtil.getEmpresaIdFromToken(token));
    }

    @Test
    void testExtraeUserId() {
        String token = jwtUtil.generateToken(7L, "user@test.com", 1L, "EDITOR");
        assertEquals(7L, jwtUtil.getUserIdFromToken(token));
    }

    @Test
    void testExtraeRol() {
        String token = jwtUtil.generateToken(1L, "user@test.com", 1L, "SOLO_LECTURA");
        assertEquals("SOLO_LECTURA", jwtUtil.getRolFromToken(token));
    }
}
