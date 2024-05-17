package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_servicio", schema = "ctdsys_sch")
public class Servicio {

    @Id
    @Column(name = "i_codserv")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_servicio_i_codserv_seq"
    )
    @SequenceGenerator(
            name = "tb_servicio_i_codserv_seq",
            sequenceName = "tb_servicio_i_codserv_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "v_tituserv")
    private String titulo;

    @Column(name = "v_desserv", nullable = false, length = 50)
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
