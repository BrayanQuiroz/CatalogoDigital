package catalogo.persistence.services;

import catalogo.persistence.dto.DatosDTO;
import catalogo.persistence.dto.PersonaJuridicaDTO;
import catalogo.persistence.dto.UsuarioDTO;
import catalogo.persistence.models.*;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import catalogo.persistence.repositories.TipoUsuarioRepository;
import catalogo.persistence.repositories.UsuarioRepository;
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
//    private final String usuarioCreador;

    @Autowired
    public DatosService(
            UsuarioRepository usuarioRepository,
            PersonaJuridicaRepository personaJuridicaRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            SendEmailService sendEmailService
//            @Value("${users.creation}") String usuarioCreador
    ) {
        this.usuarioRepository = usuarioRepository;
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.sendEmailService = sendEmailService;
//        this.usuarioCreador = usuarioCreador;
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
        personaJuridica.setGenero(personaJuridicaDTO.getGenero());
        personaJuridica.setCarreraProfesional(personaJuridicaDTO.getCarreraProfesional());
        personaJuridica.setExperienciaLaboral(personaJuridicaDTO.getExperienciaLaboral());
        personaJuridica.setRazonSocial(personaJuridicaDTO.getRazonSocial());
        personaJuridica.setDepartamentos(personaJuridicaDTO.getDepartamentos());
        personaJuridica.setNivelAcademico(personaJuridicaDTO.getNivelAcademico());
        personaJuridica.setWebsite(personaJuridicaDTO.getWebsite());
        personaJuridica.setDireccion(personaJuridicaDTO.getDireccion());
        personaJuridica.setDistrito(personaJuridicaDTO.getDistrito());
        personaJuridica.setProvincia(personaJuridicaDTO.getProvincia());

        personaJuridica.setTipoProveedor(personaJuridicaDTO.getTipoProveedor());
        personaJuridica.setDni(personaJuridicaDTO.getDni());
        Departamento departamento = new Departamento();
        departamento.setCodDep(1);
        personaJuridica.setDepartamento(departamento);
        personaJuridica.setFechreg(new Timestamp(System.currentTimeMillis()));

        CadenaProductiva cadenaProductiva = new CadenaProductiva();
        cadenaProductiva.setCodcadprod(1);
        personaJuridica.setCadenaProductiva(cadenaProductiva);

        Servicio servicio = new Servicio();
        servicio.setId(1);
        personaJuridica.setServicio(servicio);

        Sector sector = new Sector();
        sector.setCodSector(1);
        personaJuridica.setSector(sector);

        personaJuridica.setActivo(1);
        personaJuridica.setHabido(1);
        personaJuridica.setFlagUpdate((short) 0);
        personaJuridica.setFlagEstado((short)1);

        return personaJuridica;
    }
}


