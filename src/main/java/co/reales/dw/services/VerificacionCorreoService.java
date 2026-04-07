package co.reales.dw.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificacionCorreoService {

    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verificados = new ConcurrentHashMap<>();

    public String generarToken(String email) {
        String token = UUID.randomUUID().toString();
        tokens.put(email, token);
        verificados.putIfAbsent(email, false);
        return token;
    }

    public boolean verificar(String email, String token) {
        String tokenGuardado = tokens.get(email);

        if (tokenGuardado != null && tokenGuardado.equals(token)) {
            verificados.put(email, true);
            tokens.remove(email);
            return true;
        }

        return false;
    }

    public boolean estaVerificado(String email) {
        return verificados.getOrDefault(email, false);
    }
}