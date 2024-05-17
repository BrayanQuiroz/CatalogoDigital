package catalogo.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UsuarioDTO {

    @JsonProperty("ruc")
    private Long ruc;

    @JsonProperty("apellidoPaterno")
    private String apellidoPaterno;

    @JsonProperty("estado")
    private Integer estado;

    @JsonProperty("fechaNacimiento")
    private String fechaNacimiento;

    @JsonProperty("experienciaLaboral")
    private Integer experienciaLaboral;

    @JsonProperty("departamentos")
    private String departamentos;

    @JsonProperty("update")
    private Integer update;

    @JsonProperty("provincia")
    private String provincia;

    @JsonProperty("nombres")
    private String nombres;

    @JsonProperty("apellidoMaterno")
    private String apellidoMaterno;

    @JsonProperty("carreraProfesional")
    private String carreraProfesional;

    @JsonProperty("password")
    private String password;

    @JsonProperty("razonSocial")
    private String razonSocial;

    @JsonProperty("web")
    private String web;

    @JsonProperty("genero")
    private String genero;

    @JsonProperty("nivelAcademico")
    private String nivelAcademico;

    @JsonProperty("cadenaProductiva")
    private String cadenaProductiva;

    @JsonProperty("sector")
    private String sector;

    @JsonProperty("certificadoLaboral")
    private String certificadoLaboral;

    @JsonProperty("distrito")
    private String distrito;

    @JsonProperty("direccion")
    private String direccion;

    @JsonProperty("codusuario")
    private Integer codusuario;

    @JsonProperty("curriculum")
    private String curriculum;

    @JsonProperty("flagEstado")
    private Integer flagEstado;

    @JsonProperty("subSector")
    private String subSector;

    @JsonProperty("tipoProveedor")
    private String tipoProveedor;

    @JsonProperty("departamento")
    private String departamento;

    @JsonProperty("tipoUsuario")
    private String tipoUsuario;


}
