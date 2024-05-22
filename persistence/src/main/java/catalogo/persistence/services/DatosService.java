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

        personaJuridica.setRuc(personaJuridicaDTO.getRuc());
        personaJuridica.setApellidoMaterno(personaJuridicaDTO.getApellidoMaterno());
        personaJuridica.setApellidoPaterno(personaJuridicaDTO.getApellidoPaterno());
        personaJuridica.setNombres(personaJuridicaDTO.getNombres());
        personaJuridica.setCarreraProfesional(personaJuridicaDTO.getCarreraProfesional());
        personaJuridica.setExperienciaLaboral(personaJuridicaDTO.getExperienciaLaboral());
        personaJuridica.setRazonSocial(personaJuridicaDTO.getRazonSocial());
        personaJuridica.setWebsite(personaJuridicaDTO.getWebsite());
        personaJuridica.setDireccion(personaJuridicaDTO.getDireccion());
        personaJuridica.setDistrito(personaJuridicaDTO.getDistrito());
        personaJuridica.setProvincia(personaJuridicaDTO.getProvincia());
        personaJuridica.setDni(personaJuridicaDTO.getDni());


        Sector sector = new Sector();
        sector.setCodSector(personaJuridicaDTO.getSector());
        personaJuridica.setSector(sector);

        System.out.println("Aqui es el valor de lo que llega"+personaJuridicaDTO.getSector());

        Servicio servicio = new Servicio();
        servicio.setId(personaJuridicaDTO.getServicio());
        personaJuridica.setServicio(servicio);

        Genero genero = new Genero();
        genero.setId(personaJuridicaDTO.getGenero());
        personaJuridica.setGenero(genero);

        NivelAcademico nivelAcademico = new NivelAcademico();
        nivelAcademico.setId(personaJuridicaDTO.getNivelAcademico());
        personaJuridica.setNivelAcademico(nivelAcademico);

        Especialidad especialidad = new Especialidad();
        especialidad.setId(personaJuridicaDTO.getEspecialidad());
        personaJuridica.setEspecialidad(especialidad);

        Departamento departamento = new Departamento();
        departamento.setCodDep(personaJuridicaDTO.getDepartamento());
        personaJuridica.setDepartamento(departamento);

        System.out.println("Aquí esta especialidad: "+personaJuridicaDTO.getEspecialidad());

        CadenaProductiva cadenaProductiva = new CadenaProductiva();
        cadenaProductiva.setCodcadprod(personaJuridicaDTO.getCadenaProductiva());
        personaJuridica.setCadenaProductiva(cadenaProductiva);

        TipoProveedor tipoProveedor = new TipoProveedor();
        tipoProveedor.setId(personaJuridicaDTO.getIdTipoProveedor());
        personaJuridica.setTipoProveedor(tipoProveedor);

        TipoPersona tipoPersona = new TipoPersona();
        tipoPersona.setId(personaJuridicaDTO.getIdTipoPersona());
        personaJuridica.setTipoPersona(tipoPersona);
        personaJuridica.setFlagimagen((short)0);
        personaJuridica.setActivo(1);
        personaJuridica.setHabido(1);
        personaJuridica.setFlagUpdate((short) 0);
        personaJuridica.setFlagEstado((short)1);

        return personaJuridica;
    }
}


