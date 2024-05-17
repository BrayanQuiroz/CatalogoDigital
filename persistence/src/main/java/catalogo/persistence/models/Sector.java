package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_sector", schema = "ctdsys_sch")
public class Sector {

    @Id
    @Column(name = "i_codsector")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_sector_i_codsector_seq"
    )
    @SequenceGenerator(
            name = "tb_sector_i_codsector_seq",
            sequenceName = "tb_sector_i_codsector_seq",
            allocationSize = 1
    )
    private Integer codSector;

    @Column(name = "v_dessector", nullable = false, length = 50)
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
