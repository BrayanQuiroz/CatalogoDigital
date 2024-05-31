package catalogo.persistence.services;


import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.models.Usuario;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateStateService {

    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final SendEmailService sendEmailService;
    private final UsuarioService usuarioService;

    public UpdateStateService(PersonaJuridicaRepository personaJuridicaRepository, SendEmailService sendEmailService, UsuarioService usuarioService) {
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.sendEmailService = sendEmailService;
        this.usuarioService = usuarioService;
    }


    public PersonaJuridica updateEstado(String destinatario,Long ruc, Short flagUpdate,String razonSocial ,String usumod) {
        try {
            Usuario usuario = usuarioService.getUsuarioByRuc(ruc);
            PersonaJuridica personaJuridica = personaJuridicaRepository.findByRuc(ruc)
                    .orElseGet(() -> personaJuridicaRepository.findByUsuarioAndFlagEstadoAndFlagUpdate(usuario, (short) 1,(short)1))
                    .orElseGet(() -> personaJuridicaRepository.findByUsuarioAndFlagEstadoAndFlagUpdate(usuario, (short) 1,(short)1));
            personaJuridica.setFlagUpdate(flagUpdate);
            personaJuridica.setUsumod(usumod);

//            String destintario, Long ruc, String password, String razonSocial, int tipo
            sendEmailService.SendEmail(destinatario, ruc, null, razonSocial, 1);
            return personaJuridicaRepository.save(personaJuridica)

        }catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado de la persona jurídica", e);
        }
    }
}
