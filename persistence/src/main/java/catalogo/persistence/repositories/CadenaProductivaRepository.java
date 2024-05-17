package catalogo.persistence.repositories;

import catalogo.persistence.models.CadenaProductiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadenaProductivaRepository extends JpaRepository<CadenaProductiva, Integer> {
    CadenaProductiva findCadenaProductivaByCodcadprod(Integer codcadprod);
}
