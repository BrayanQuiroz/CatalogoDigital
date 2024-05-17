package catalogo.persistence.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PersonaJuridicaDTO {

    private Long ruc;
    private String razonSocial;
    private String direccion;
    private String departamentos;
    private String provincia;
    private String distrito;
    private String nombres;
    private String genero;
    private Integer activo;
    private Integer dni;
    private Integer habido;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechaNacimiento;
    private String tipoProveedor;
    private String nivelAcademico;
    private String carreraProfesional;
    private Short experienciaLaboral;
    private String website;
    private Integer departamento;
    private Integer cadenaProductiva;
    private Integer sector;
}
