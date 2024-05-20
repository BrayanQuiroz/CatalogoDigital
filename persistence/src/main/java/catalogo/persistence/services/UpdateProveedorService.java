package catalogo.persistence.services;

import catalogo.persistence.dto.UpdateProveedorDTO;
import catalogo.persistence.models.*;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateProveedorService  extends PersonaJuridicaService{

    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;
    private final CadenaProductivaService cadenaProductivaService;
    private final ServicioService servicioService;
    private final SectorService sectorService;


    @Autowired
    public UpdateProveedorService(PersonaJuridicaRepository personaJuridicaRepository, UsuarioService usuarioService, DepartamentoService departamentoService, CadenaProductivaService cadenaProductivaService, ServicioService servicioService, SectorService sectorService) {
        super(personaJuridicaRepository);
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.usuarioService = usuarioService;
        this.departamentoService = departamentoService;
        this.cadenaProductivaService = cadenaProductivaService;
        this.servicioService = servicioService;
        this.sectorService = sectorService;
    }

    public void updateDatos(Integer codUsuario) {
        List<PersonaJuridica> observadas = getActivePJuridicasByCodUsuario(codUsuario);
    }

    public void getProveedorByRuc(Long ruc,UpdateProveedorDTO updateProveedorDTO){

        try{
            System.out.println("Buscando usuario con RUC: " + ruc);
            Usuario usuario = usuarioService.getUsuarioByRuc(ruc);


            System.out.println("Aquí el ruc "+usuario);

            if (usuario == null) {

                throw new RuntimeException("No existe un usuario con el RUC proporcionado, no se puede actualizar datos.");
            }

            PersonaJuridica personaJuridica = new PersonaJuridica();

            Usuario codUsuario = usuarioService.getUsuarioByUsuario(updateProveedorDTO.getUsuario());

            personaJuridica.setUsuario(codUsuario);


            personaJuridica.setFlagUpdate((short)0);
            personaJuridica.setFlagEstado((short)1);

            personaJuridicaRepository.save(personaJuridica);

        }catch (Exception e){
            throw new RuntimeException("Error: ",e);
        }
    }

}
