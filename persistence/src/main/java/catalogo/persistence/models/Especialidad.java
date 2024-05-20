package catalogo.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_especialidad", schema = "ctdsys_sch")
public class Especialidad {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tb_especialidad_i_codespecialidad_seq"
    )
    @SequenceGenerator(
            name = "tb_especialidad_i_codespecialidad_seq",
            sequenceName = "ctdsys_sch.tb_especialidad_i_codespecialidad_seq",
            allocationSize = 1
    )
    @Column(name = "i_codespecialidad")
    private Integer id;

    @Column(name = "v_desespecialidad", nullable = false, length = 50)
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