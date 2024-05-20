package catalogo.persistence.services;

import catalogo.persistence.models.TipoPersona;
import catalogo.persistence.repositories.TipoPersonaRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoPersonService {

    private final TipoPersonaRepository tipoPersonaRepository;

    public TipoPersonService(TipoPersonaRepository tipoPersonaRepository) {
        this.tipoPersonaRepository = tipoPersonaRepository;
    }

    public TipoPersona getTipoPersona(Integer id) {
        return tipoPersonaRepository.findTipoPersonaById(id);
    }
}
