package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "TB_TIPOPROVEEDOR", schema = "ctdsys_sch")
public class TipoProveedor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_tipoproveedor_i_codtipoprov_seq"
    )
    @SequenceGenerator(
            name = "tb_tipoproveedor_i_codtipoprov_seq",
            sequenceName = "ctdsys_sch.tb_tipoproveedor_i_codtipoprov_seq",
            allocationSize = 1
    )
    @Column(name = "i_codtipoprov")
    private Integer id;

    @Column(name = "v_destipoprov", nullable = false, length = 50)
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
