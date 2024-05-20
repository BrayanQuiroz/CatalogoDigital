package catalogo.persistence.services;

import catalogo.persistence.dto.DatosDTO;
import catalogo.persistence.dto.PersonaJuridicaDTO;
import catalogo.persistence.dto.UsuarioDTO;
import catalogo.persistence.models.*;
import catalogo.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class DatosService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SendEmailService sendEmailService;
    private final DepartamentoRepository departamentoRepository;
    private final CadenaProductivaRepository cadenaProductivaRepository;
    private final ServicioService servicioService;
    private final SectorRepository sectorRepository;
//    private final String usuarioCreador;

    @Autowired
    public DatosService(
            UsuarioRepository usuarioRepository,
            PersonaJuridicaRepository personaJuridicaRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            SendEmailService sendEmailService,
//            @Value("${users.creation}") String usuarioCreador
            DepartamentoRepository departamentoRepository, CadenaProductivaRepository cadenaProductivaRepository, ServicioService servicioService, SectorRepository sectorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.sendEmailService = sendEmailService;
//        this.usuarioCreador = usuarioCreador;
        this.departamentoRepository = departamentoRepository;
        this.cadenaProductivaRepository = cadenaProductivaRepository;
        this.servicioService = servicioService;
        this.sectorRepository = sectorRepository;
    }

    public void guardarDatos(DatosDTO datos) {
        Usuario usuario = convertirUsuario(datos.getUsuario());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        PersonaJuridica personaJuridica = convertirPersonaJuridica(datos.getPersonaJuridica());
        personaJuridica.setUsuario(usuarioGuardado);
        personaJuridicaRepository.save(personaJuridica);

        sendEmailService.SendEmail(datos.getUsuario().getCorreo(), datos.getUsuario().getRuc(),
                datos.getUsuario().getPassword(), datos.getPersonaJuridica().getRazonSocial());
    }


    private Usuario convertirUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setRuc(usuarioDTO.getRuc());
        usuario.setCorreo(usuarioDTO.getCorreo());

        String passwordGenerate;
        passwordGenerate =  "itp2024$";

        String hashedPassword = BCrypt.hashpw(passwordGenerate, BCrypt.gensalt());
        usuario.setPassword(hashedPassword);

        usuario.setFlagEstado((short) 1);
        usuario.setUsureg("ADMINISTRADOR");
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(3);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setFechreg(new Timestamp(System.currentTimeMillis()));

        return usuario;
    }

    private PersonaJuridica convertirPersonaJuridica(PersonaJuridicaDTO personaJuridicaDTO) {
        PersonaJuridica personaJuridica = new PersonaJuridica();


        Departamento departamento = departamentoRepository
                .findDepartamentoByCodDep(personaJuridicaDTO.getDepartamento());

        CadenaProductiva cadenaProductiva = cadenaProductivaRepository
                .findCadenaProductivaByCodcadprod(personaJuridicaDTO.getCadenaProductiva());

        Servicio servicio = servicioService
                .getServicioByCodServicio(personaJuridicaDTO.getServicio());

        Sector sector = sectorRepository
                .findSectorByCodSector(personaJuridicaDTO.getSector());

        System.out.println(personaJuridicaDTO.getSector());
        System.out.println(personaJuridicaDTO.getServicio());
        System.out.println(personaJuridicaDTO.getCadenaProductiva());

        personaJuridica.setRuc(personaJuridicaDTO.getRuc());
        personaJuridica.setApellidoMaterno(personaJuridicaDTO.getApellidoMaterno());
        personaJuridica.setApellidoPaterno(personaJuridicaDTO.getApellidoPaterno());
        personaJuridica.setNombres(personaJuridicaDTO.getNombres());

        personaJuridica.setCarreraProfesional(personaJuridicaDTO.getCarreraProfesional());
        personaJuridica.setExperienciaLaboral(personaJuridicaDTO.getExperienciaLaboral());
        personaJuridica.setRazonSocial(personaJuridicaDTO.getRazonSocial());

        personaJuridica.setWebsite(personaJuridicaDTO.getWebsite());

        personaJuridica.setDepartamento(departamento);
        personaJuridica.setCadenaProductiva(cadenaProductiva);
        personaJuridica.setServicio(servicio);
        personaJuridica.setSector(sector);

        personaJuridica.setFechreg(new Timestamp(System.currentTimeMillis()));



        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(1);
        personaJuridica.setTipoPersona(tipoPersona);

        TipoProveedor tipoProveedor = new TipoProveedor();
        tipoProveedor.setId(2);



        personaJuridica.setActivo(1);
        personaJuridica.setHabido(1);
        personaJuridica.setFlagUpdate((short) 0);
        personaJuridica.setFlagEstado((short)1);

        return personaJuridica;
    }
}


