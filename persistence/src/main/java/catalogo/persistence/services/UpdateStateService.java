package catalogo.persistence.services;


import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateStateService {

    private final PersonaJuridicaRepository personaJuridicaRepository;

    public UpdateStateService(PersonaJuridicaRepository personaJuridicaRepository) {
        this.personaJuridicaRepository = personaJuridicaRepository;
    }


    public PersonaJuridica updateEstado(Long ruc, Short flagUpdate, String usumod) {
        try {
            PersonaJuridica personaJuridica = personaJuridicaRepository.findByRuc(ruc)
                    .orElseThrow(() -> new EntityNotFoundException("Persona jurídica no encontrada con el RUC: " + ruc));
            personaJuridica.setFlagUpdate(flagUpdate);
            personaJuridica.setUsumod(usumod);
            return personaJuridicaRepository.save(personaJuridica);

        }catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado de la persona jurídica", e);
        }
    }
}
