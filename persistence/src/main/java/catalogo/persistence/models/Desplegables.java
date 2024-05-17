package catalogo.persistence.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tb_desplegables", schema = "ctdsys_sch")
public class Desplegables {

    @Id
    @Column(name = "i_coddespl")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_desplegables_i_coddespl_seq"
    )
    @SequenceGenerator(
            name = "tb_desplegables_i_coddespl_seq",
            sequenceName = "tb_desplegables_i_coddespl_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "n_codtipodes", nullable = false, length = 50)
    private String codigoTipoDes;

    @Column(name = "s_flagestado", nullable = false)
    private Short flagEstado;

    @Column(name = "v_usureg", length = 20)
    private String usuarioRegistro;

    @Column(name = "v_usumod", length = 20)
    private String usumod;

    @Column(name = "t_fechreg")
    private Timestamp fechreg;

    @Column(name = "t_fechmod")
    private Timestamp fechmod;
}


