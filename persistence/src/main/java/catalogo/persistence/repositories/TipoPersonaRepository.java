package catalogo.persistence.repositories;

import catalogo.persistence.models.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPersonaRepository extends JpaRepository<TipoPersona, Integer> {

    TipoPersona findTipoPersonaById(Integer id);
}
