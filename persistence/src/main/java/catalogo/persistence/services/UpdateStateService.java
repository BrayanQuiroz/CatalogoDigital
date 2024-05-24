package catalogo.persistence.services;


import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateStateService {

    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final SendEmailService sendEmailService;

    public UpdateStateService(PersonaJuridicaRepository personaJuridicaRepository, SendEmailService sendEmailService) {
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.sendEmailService = sendEmailService;
    }


    public PersonaJuridica updateEstado(String destinatario,Long ruc, Short flagUpdate,String razonSocial ,String usumod) {
        try {
            PersonaJuridica personaJuridica = personaJuridicaRepository.findByRuc(ruc)
                    .orElseThrow(() -> new EntityNotFoundException("Persona jurídica no encontrada con el RUC: " + ruc));
            personaJuridica.setFlagUpdate(flagUpdate);
            personaJuridica.setUsumod(usumod);

//            String destintario, Long ruc, String password, String razonSocial, int tipo
            sendEmailService.SendEmail(destinatario, ruc, null, razonSocial, 1);
            return personaJuridicaRepository.save(personaJuridica);

        }catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado de la persona jurídica", e);
        }
    }
}
