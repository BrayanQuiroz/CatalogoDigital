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

    public UpdateStateService(PersonaJuridicaRepository personaJuridicaRepository,
                              SendEmailService sendEmailService, UsuarioService usuarioService) {
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.sendEmailService = sendEmailService;
        this.usuarioService = usuarioService;
    }


    public PersonaJuridica updateEstado(String destinatario,Long ruc,
                                        Short flagUpdate,String razonSocial ,String usumod, Integer tipo) {
        try {
            Usuario usuario = usuarioService.getUsuarioByRuc(ruc);

            switch (tipo){

                case 1 ->{
                    PersonaJuridica personaJuridica = personaJuridicaRepository.findByRuc(ruc)
                            .orElseThrow(() -> new EntityNotFoundException("Persona jurídica no encontrada con el RUC: " + ruc));
                    personaJuridica.setFlagUpdate(flagUpdate);
                    personaJuridica.setUsumod(usumod);
                    sendEmailService.SendEmail(destinatario, ruc, null, razonSocial, 2);
                    return personaJuridicaRepository.save(personaJuridica);
                }

                case 2 ->{
                    PersonaJuridica personaJuridica = personaJuridicaRepository
                            .findByUsuarioAndFlagEstadoAndFlagUpdate(usuario, (short) 1,(short)1);
                    personaJuridica.setFlagUpdate(flagUpdate);
                    personaJuridica.setUsumod(usumod);
                    sendEmailService.SendEmail(destinatario, ruc, null, razonSocial, 3);
                    return personaJuridicaRepository.save(personaJuridica);
                }

//                case 3 ->{
//
//                }

                default -> throw new IllegalArgumentException("No se pudo actualizar la persona juridica");
            }

            //String destintario, Long ruc, String password, String razonSocial, int tipo

        }catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado de la persona jurídica", e);
        }
    }
}
