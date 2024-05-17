package catalogo.persistence.services;

import catalogo.persistence.models.CadenaProductiva;
import catalogo.persistence.repositories.CadenaProductivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadenaProductivaService {

    private final CadenaProductivaRepository cadenaProductivaRepository;

    @Autowired
    public CadenaProductivaService(CadenaProductivaRepository cadenaProductivaRepository){
        this.cadenaProductivaRepository = cadenaProductivaRepository;
    }

    public CadenaProductiva getCadenaProductivaByCodCadProd(Integer codcadprod){
        return cadenaProductivaRepository.findCadenaProductivaByCodcadprod(codcadprod);
    }
}
