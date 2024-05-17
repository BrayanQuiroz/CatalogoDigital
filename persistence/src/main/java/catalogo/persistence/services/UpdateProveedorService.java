package catalogo.persistence.services;

import catalogo.persistence.repositories.PersonaJuridicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProveedorService {

    private final PersonaJuridicaRepository personaJuridicaRepository;

    @Autowired
    public UpdateProveedorService(PersonaJuridicaRepository personaJuridicaRepository) {
        this.personaJuridicaRepository = personaJuridicaRepository;
    }

}
