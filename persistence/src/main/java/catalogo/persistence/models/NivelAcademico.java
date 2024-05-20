package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_nivelacademico", schema = "ctdsys_sch")
public class NivelAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "tb_nivelacademico_i_codnivelac_seq"
    )
    @SequenceGenerator(
            name = "tb_nivelacademico_i_codnivelac_seq",
            sequenceName = "ctdsys_sch.tb_nivelacademico_i_codnivelac_seq",
            allocationSize = 1
    )
    @Column(name = "i_codnivelac")
    private Integer id;

    @Column(name = "v_desnivelac", nullable = false, length = 50)
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
