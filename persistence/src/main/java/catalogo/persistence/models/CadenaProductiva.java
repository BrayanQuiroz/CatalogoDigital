package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name ="tb_cadenaproductiva", schema = "ctdsys_sch")
public class CadenaProductiva {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_cadenaproductiva_i_codcadprod_seq"
    )
    @SequenceGenerator(
            name = "tb_cadenaproductiva_i_codcadprod_seq",
            sequenceName = "ctdsys_sch.tb_cadenaproductiva_i_codcadprod_seq",
            allocationSize = 1
    )

    @Column(name = "i_codcadprod", nullable = false)
    private Integer codcadprod;

    @Column(name = "v_descadprod", nullable = false)
    private String descadprod;

    @Column(name = "s_flagestado", nullable = false)
    private Short flagestado;

    @Column(name = "v_usureg")
    private String usureg;

    @Column(name = "v_usumod")
    private String usumod;

    @Column(name = "t_fechreg")
    private Timestamp fechreg;

    @Column(name = "t_fechmod")
    private Timestamp fechmod;
}
