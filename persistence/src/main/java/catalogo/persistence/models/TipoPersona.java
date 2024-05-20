package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "TB_TIPOPERSONA", schema = "CTDSYS_SCH")
public class TipoPersona {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_tipopersona_i_codtipoper_seq"
    )
    @SequenceGenerator(
            name = "tb_tipopersona_i_codtipoper_seq",
            sequenceName = "ctdsys_sch.tb_tipopersona_i_codtipoper_seq",
            allocationSize = 1
    )
    @Column(name = "i_codtipoper")
    private Integer id;

    @Column(name = "v_destipoper", nullable = false, length = 50)
    private String descripcion;

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