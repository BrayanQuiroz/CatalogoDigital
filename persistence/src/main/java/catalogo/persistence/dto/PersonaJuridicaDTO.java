package catalogo.persistence.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PersonaJuridicaDTO {

    private Long ruc;
    private String razonSocial;
    private String direccion;
    private String provincia;
    private String distrito;
    private String nombres;
    private Integer activo;
    private Integer dni;
    private Integer habido;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechaNacimiento;
    private Integer idTipoProveedor;
    private Integer idTipoPersona;
    private Integer genero;
    private Integer NivelAcademico;
    private String carreraProfesional;
    private Integer especialidad;
    private Short experienciaLaboral;
    private String website;
    private Integer departamento;
    private Integer cadenaProductiva;
    private Integer sector;
    private Integer servicio;
    private Short flagimagen;

}
