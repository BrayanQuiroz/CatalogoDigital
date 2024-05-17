package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "mv_personajuridica" , schema = "ctdsys_sch")
public class PersonaJuridica {


    @Id
    @Column(name = "i_codpersonjuridica")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mv_personajuridica_i_codpersonjuridica_seq"
    )
    @SequenceGenerator(
            name = "mv_personajuridica_i_codpersonjuridica_seq",
            sequenceName = "ctdsys_sch.mv_personajuridica_i_codpersonjuridica_seq",
            allocationSize = 1
    )
    private Integer codPersonajurida;

    @Column(name = "b_ruc", unique = true, nullable = false)
    private Long ruc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "i_codusuario")
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "i_codcadprod")
    private CadenaProductiva cadenaProductiva;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "i_codserv")
    private Servicio servicio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "i_codsector")
    private Sector sector;

    @Column(name = "v_razonsocial", nullable = false, length = 80)
    private String razonSocial;


    @Column(name = "i_activo")
    private Integer activo;

    @Column(name = "i_habido")
    private Integer habido;

    @Column(name = "v_direccion",length = 100)
    private String direccion;

    @Column(name = "v_departamento",  length = 25)
    private String departamentos;

    @Column(name = "i_dni",  length = 8)
    private  Integer dni;

    @Column(name = "v_provincia", length = 25)
    private String provincia;

    @Column(name = "v_distrito", length = 25)
    private String distrito;

    @Column(name = "v_nombres",  length = 22)
    private String nombres;

    @Column(name = "v_genero", length = 6)
    private String genero;

    @Column(name = "v_apellidopater", length = 18)
    private String apellidoPaterno;

    @Column(name = "v_apellidomater", length = 18)
    private String apellidoMaterno;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "i_coddep")
    private Departamento departamento;

    @Column(name = "d_fechnacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(name = "v_tipoprov", nullable = false, length = 23)
    private String tipoProveedor;

    @Column(name = "v_nivelacadem", nullable = false, length = 15)
    private String nivelAcademico;

    @Column(name = "v_carreraprofe", length = 50)
    private String carreraProfesional;

    @Column(name = "s_explaboral", nullable = false)
    private Short experienciaLaboral;

    @Column(name = "v_certilab", length = 50)
    private String certificadoLaboral;

    @Column(name = "v_curriculum", length = 50)
    private String curriculum;

    @Column(name = "v_website", length = 100)
    private String website;

    @Column(name = "s_flagestado", nullable = false)
    private Short flagEstado;

    @Column(name = "s_flagupdate", nullable = false)
    private Short flagUpdate;

    @Column(name = "v_usureg", length = 20)
    private String usureg;

    @Column(name = "v_usumod", length = 20)
    private String usumod;

    @Column(name = "t_fechreg")
    private Timestamp fechreg;

    @Column(name = "t_fechmod")
    private Timestamp fechmod;
}
