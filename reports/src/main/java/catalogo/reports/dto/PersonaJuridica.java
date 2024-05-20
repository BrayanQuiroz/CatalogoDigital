package catalogo.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;


@Data
public class PersonaJuridica {
    @JsonProperty("ruc")
    private long ruc;
    @JsonProperty("dni")
    private long dni;
    @JsonProperty("nombres")
    private String nombres;
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
    @JsonProperty("distrito")
    private String distrito;
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
    private Integer sector;
    @JsonProperty("direccion")
    private String direccion;
    @JsonProperty("tipoProveedor")
    private Integer tipoProveedor;
    @JsonProperty("departamento")
    private Integer departamento;
    @JsonProperty("servicio")
    private Integer servicio;
    @JsonProperty("especialidad")
    private Integer especialidad;


}
