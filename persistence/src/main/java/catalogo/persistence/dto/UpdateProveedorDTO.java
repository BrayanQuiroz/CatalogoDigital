package catalogo.persistence.dto;

import lombok.Data;

@Data
public class UpdateProveedorDTO {

    private String celular;
    private String correo;
    private String nivelAcademico;
    private Short experienciaLaboral;
    private String website;
    private Integer departamento;
    private Integer cadenaProductiva;
    private Integer sector;
    private Integer servicio;
}
