package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_tipousuario", schema = "ctdsys_sch")
public class TipoUsuario {

    @Id
    @Column(name = "i_codtipousu")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_tipousuario_i_codtipousu_seq"
    )
    @SequenceGenerator(
            name = "tb_tipousuario_i_codtipousu_seq",
            sequenceName = "tb_tipousuario_i_codtipousu_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "v_destipousu", nullable = false, length = 25)
    private String desTipoUsu;

    @Column(name = "s_flagestado", nullable = false)
    private Short flagEstado;

    @Column(name = "v_usureg", length = 20)
    private String usuarioRegistro;

    @Column(name = "v_usumod", length = 20)
    private String usuarioModificacion;

    @Column(name = "t_fechreg")
    private Timestamp fechaRegistro;

    @Column(name = "t_fechmod")
    private Timestamp fechaModificacion;
}
