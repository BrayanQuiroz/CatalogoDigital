package catalogo.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioDTO {

    @JsonProperty("ruc")
    private Long ruc;
    @JsonProperty("correo")
    private String correo;
}
