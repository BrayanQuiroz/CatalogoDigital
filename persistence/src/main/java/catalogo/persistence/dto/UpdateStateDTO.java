package catalogo.persistence.dto;

import lombok.Data;

@Data
public class UpdateStateDTO {
    private Long ruc;
    private Short flagUpdate;
    private String usumod;
    private String correo;
    private String razonSocial;
}
