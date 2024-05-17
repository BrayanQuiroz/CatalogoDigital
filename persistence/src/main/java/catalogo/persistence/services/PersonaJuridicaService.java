package catalogo.persistence.services;

import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.models.Usuario;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaJuridicaService {


    private final PersonaJuridicaRepository personaJuridicaRepository;

    @Autowired
    public PersonaJuridicaService(PersonaJuridicaRepository personaJuridicaRepository) {
        this.personaJuridicaRepository = personaJuridicaRepository;
    }

    public PersonaJuridica getPersonaJuridicaByCodUsuario(Integer codUsuario) {
        return personaJuridicaRepository.findByUsuarioCodUsuario(codUsuario);
    }

    public PersonaJuridica getActivePJuridicaByCodUsuario(Usuario usuario) {
        return personaJuridicaRepository.findByUsuarioAndFlagEstadoAndFlagUpdate(usuario, (short) 1, (short) 1);
    }

    public List<PersonaJuridica> getAllActivePJuridicas() {
        return personaJuridicaRepository.findAllByFlagEstadoAndFlagUpdate((short) 1, (short) 1);
    }

    //Aprobado
    public List<PersonaJuridica> getActivePJuridicasByCodUsuario(Integer codUsuario) {
        return personaJuridicaRepository.findAllByUsuarioCodUsuarioAndFlagEstadoAndFlagUpdate(codUsuario, (short) 1, (short) 1);
    }

    //Pendiente
    public List<PersonaJuridica> getActivePJuridicasByCodUsuarioTwo(Integer codUsuario) {
        return personaJuridicaRepository.findAllByUsuarioCodUsuarioAndFlagEstadoAndFlagUpdate(codUsuario, (short) 1, (short) 0);
    }

    //Observado
    public List<PersonaJuridica> getActivePJuridicasByCodUsuarioThree(Integer codUsuario) {
        return personaJuridicaRepository.findAllByUsuarioCodUsuarioAndFlagEstadoAndFlagUpdate(codUsuario, (short) 1, (short) 2);
    }

    //Rechazado
    public List<PersonaJuridica> getActivePJuridicasByCodUsuarioFour(Integer codUsuario) {
        return personaJuridicaRepository.findAllByUsuarioCodUsuarioAndFlagEstadoAndFlagUpdate(codUsuario, (short) 1, (short) 3);
    }
}
