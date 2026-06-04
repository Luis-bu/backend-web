package co.reales.dw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String correo;
    private String nombre;
    private String rol;
    private Long empresaId;
}
