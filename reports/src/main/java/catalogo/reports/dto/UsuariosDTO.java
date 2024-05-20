package catalogo.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuariosDTO {

    @JsonProperty("ruc")
    private long ruc;

    @JsonProperty("dni")
    private long dni;
    @JsonProperty("correo")
    private String correo;
    @JsonProperty("apellidoPaterno")
    private String apellidoPaterno;
    @JsonProperty("apellidoMaterno")
    private String apellidoMaterno;
    @JsonProperty("experienciaLaboral")
    private Integer experienciaLaboral;
    @JsonProperty("departamentos")
    private String departamentos;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("nombres")
    private String nombres;
    @JsonProperty("carreraProfesional")
    private String carreraProfesional;
    @JsonProperty("razonSocial")
    private String razonSocial;
    @JsonProperty("web")
    private String web;
    @JsonProperty("genero")
    private Integer genero;
    @JsonProperty("nivelAcademico")
    private Integer nivelAcademico;
    @JsonProperty("cadenaProductiva")
    private Integer cadenaProductiva;
    @JsonProperty("sector")
    private String sector;
    @JsonProperty("distrito")
    private String distrito;
    @JsonProperty("direccion")
    private String direccion;
    @JsonProperty("tipoProveedor")
    private Integer tipoProveedor;
    @JsonProperty("departamento")
    private Integer departamento;
    @JsonProperty("especialidad")
    private Integer especialidad;
}
