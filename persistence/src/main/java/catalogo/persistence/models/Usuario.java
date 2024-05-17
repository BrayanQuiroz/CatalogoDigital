package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name="mv_usuario", schema = "ctdsys_sch")
public class Usuario {

    @Id
    @Column(name = "i_codusuario")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mv_usuario_i_codusuario_seq"
    )
    @SequenceGenerator(
            name = "mv_usuario_i_codusuario_seq",
            sequenceName = "ctdsys_sch.mv_usuario_i_codusuario_seq",
            allocationSize = 1
    )
    private Integer codUsuario;

    @Column(name = "b_ruc", nullable = false)
    private Long ruc;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "i_codtipousu")
    private TipoUsuario tipoUsuario;

    @Column(name = "v_password",  length = 60)
    private String password;
    @Column(name = "v_correo",  length = 40)
    private String correo;

    @Column(name = "s_flagestado")
    private Short flagEstado;

    @Column(name = "v_usureg", length = 20)
    private String usureg;

    @Column(name = "v_usumod", length = 20)
    private String usumod;

    @Column(name = "t_fechreg")
    private Timestamp fechreg;

    @Column(name = "t_fechmod")
    private Timestamp fechmod;
}
