package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_departamento", schema = "ctdsys_sch")
public class Departamento {

    @Id
    @Column(name = "i_coddep")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_departamento_i_coddep_seq"
    )
    @SequenceGenerator(
            name = "tb_departamento_i_coddep_seq",
            sequenceName = "tb_departamento_i_coddep_seq",
            allocationSize = 1
    )
    private Integer codDep;

    @Column(name = "v_desdep", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "s_flagestado", nullable = false)
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
