package catalogo.persistence.repositories;

import catalogo.persistence.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {
    Sector findSectorByCodSector(Integer codSector);
}
