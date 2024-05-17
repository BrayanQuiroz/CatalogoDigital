package catalogo.persistence.services;

import catalogo.persistence.models.Sector;
import catalogo.persistence.repositories.SectorRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectorService  {

    private final SectorRepository sectorRepository;

    @Autowired
    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public Sector getSectorByCodSector(Integer id) {
        return sectorRepository.findSectorByCodSector(id);
    }

}
