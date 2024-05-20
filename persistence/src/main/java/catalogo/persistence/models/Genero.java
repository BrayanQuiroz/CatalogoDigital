package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_genero", schema = "ctdsys_sch")
public class Genero {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_genero_i_codgenero_seq"
    )
    @SequenceGenerator(
            name = "tb_genero_i_codgenero_seq",
            schema = "ctdsys_sch.tb_genero_i_codgenero_seq",
            allocationSize = 1
    )
    @Column(name = "i_codgenero")
    private Integer id;

    @Column(name = "v_desgenero", nullable = false, length = 50)
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
