package catalogo.persistence.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long ruc;
    private String password;
    private  String correo;
    private Short flagEstado;
}
