package catalogo.persistence.repositories;

import catalogo.persistence.models.Genero;
import catalogo.persistence.models.NivelAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelAcademicoRepository extends JpaRepository<NivelAcademico, Integer> {

    NivelAcademico findNivelAcademicoById(Integer id);
}
