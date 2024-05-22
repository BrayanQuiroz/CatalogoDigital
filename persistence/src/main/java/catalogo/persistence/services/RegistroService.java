package catalogo.persistence.services;

import catalogo.persistence.dto.PersonaJuridicaDTO;
import catalogo.persistence.dto.UsuarioDTO;
import catalogo.persistence.models.*;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import catalogo.persistence.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;

@Service
public class RegistroService {


    private final UsuarioRepository usuarioRepository;

    private final PersonaJuridicaRepository personaJuridicaRepository;

    //Se agrega final para la inmutabilidad
    private SendEmailService sendEmailService;

    @Autowired
    public RegistroService(UsuarioRepository usuarioRepository,
                           PersonaJuridicaRepository personaJuridicaRepository,
                           SendEmailService sendEmailService){

        this.usuarioRepository = usuarioRepository;
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.sendEmailService = sendEmailService;
    }


    @Transactional
    public void registrarProveedor(UsuarioDTO usuarioDTO,
                                   PersonaJuridicaDTO personaJuridicaDTO,
                                   MultipartFile pdfCV,
                                   MultipartFile pdfAutorizacion,
                                   MultipartFile pdfCertificado,
                                   MultipartFile imagen,
                                   MultipartFile pdfFichaRuc) throws Exception {

        Usuario usuarioExiste = usuarioRepository.findByRuc(Long.parseLong(String.valueOf(usuarioDTO.getRuc())));

        if(usuarioExiste != null){
            throw new ResponseStatusException(HttpStatus.LENGTH_REQUIRED, "El RUC " + usuarioDTO.getRuc() + " ya se encuentra registrado");
        }

        String passwordGenerate = "itp2024$";
        String hashedPassword = BCrypt.hashpw(passwordGenerate, BCrypt.gensalt(10));
        System.out.println("Hashed: " + hashedPassword);

// Verificar la contraseña
        boolean match = BCrypt.checkpw(passwordGenerate, hashedPassword);
        System.out.println("Match: " + match);

        Usuario usuario = new Usuario();
        usuario.setRuc(usuarioDTO.getRuc());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setFlagEstado((short) 1);
        usuario.setUsureg(String.valueOf(usuarioDTO.getRuc()));
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(3);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setPassword(hashedPassword);
        usuario.setFechreg(new Timestamp(System.currentTimeMillis()));

        PersonaJuridica personaJuridica = new PersonaJuridica();
        personaJuridica.setRuc(personaJuridicaDTO.getRuc());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        personaJuridica.setUsuario(usuarioGuardado);

        personaJuridica.setApellidoMaterno(personaJuridicaDTO.getApellidoMaterno());
        personaJuridica.setApellidoPaterno(personaJuridicaDTO.getApellidoPaterno());
        personaJuridica.setNombres(personaJuridicaDTO.getNombres());


        personaJuridica.setExperienciaLaboral(personaJuridicaDTO.getExperienciaLaboral());
        personaJuridica.setRazonSocial(personaJuridicaDTO.getRazonSocial());

        personaJuridica.setWebsite(personaJuridicaDTO.getWebsite());
        personaJuridica.setDireccion(personaJuridicaDTO.getDireccion());
        personaJuridica.setDistrito(personaJuridicaDTO.getDistrito());
        personaJuridica.setProvincia(personaJuridicaDTO.getProvincia());

        personaJuridica.setDni(personaJuridicaDTO.getDni());
        personaJuridica.setFechreg(new Timestamp(System.currentTimeMillis()));
        personaJuridica.setUsureg(String.valueOf(personaJuridicaDTO.getRuc()));

        Departamento departamento = new Departamento();
        departamento.setCodDep(1);
        personaJuridica.setDepartamento(departamento);

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

        if (usuario.getCodUsuario() == null) {
            throw new Exception("Fallo al guardar el usuario.");
        }

        if (pdfCV != null && !pdfCV.isEmpty()) {
            saveDocument(pdfCV, String.valueOf(usuarioDTO.getRuc()), 1);
        }
        if (pdfAutorizacion != null && !pdfAutorizacion.isEmpty()) {
            saveDocument(pdfAutorizacion, String.valueOf(usuarioDTO.getRuc()), 2);
        }
        if (pdfCertificado != null && !pdfCertificado.isEmpty()) {
            saveDocument(pdfCertificado, String.valueOf(usuarioDTO.getRuc()), 3);
        }
        if (imagen != null && !imagen.isEmpty()) {
            saveDocument(imagen, String.valueOf(usuarioDTO.getRuc()), 4);
        }

        if (pdfFichaRuc != null && !pdfFichaRuc.isEmpty()) {
            saveDocument(pdfFichaRuc, String.valueOf(usuarioDTO.getRuc()), 4);
        }

        personaJuridica.setUsuario(usuario);
        personaJuridicaRepository.save(personaJuridica);

        sendEmailService.SendEmail(usuarioDTO.getCorreo(), usuarioDTO.getRuc(), usuarioDTO.getPassword(),
                                    personaJuridicaDTO.getRazonSocial());

    }

    public void saveDocument(MultipartFile file, String ruc, Integer tipo)
            throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo proporcionado está vacío.");
        }

        // Configuración de las rutas de los directorios Autorizacion
        String directoryPath = "/opt/data/autorizacion/";
        String directoryPathTwo = "/opt/data/cul/";
        String directoryPathThree = "/opt/data/cv/";
        String directoryPathFour = "/opt/data/imagenes/";
        String directoryPathFive = "/opt/data/fichaRuc/";

        // Crear los directorios si no existen
        File autorizacionDirectory = new File(directoryPath);
        if (!autorizacionDirectory.exists() && !autorizacionDirectory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de currículums en: " + autorizacionDirectory.getAbsolutePath());
        }

        File certificateDirectory = new File(directoryPathTwo);
        if (!certificateDirectory.exists() && !certificateDirectory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de certificados en: " + certificateDirectory.getAbsolutePath());
        }

        File curriculumDirectory = new File(directoryPathThree);
        if (!curriculumDirectory.exists() && !curriculumDirectory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de currículums en: " + curriculumDirectory.getAbsolutePath());
        }

        File imagesDirectory = new File(directoryPathFour);
        if (!imagesDirectory.exists() && !imagesDirectory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de currículums en: " + imagesDirectory.getAbsolutePath());
        }


        File FichaDirectory = new File(directoryPathFive);
        if (!FichaDirectory.exists() && !FichaDirectory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de certificados en: " + FichaDirectory.getAbsolutePath());
        }

        Path path;
        switch (tipo) {
            case 1: // Currículum
                path = Paths.get(directoryPathTwo, ruc + "-cul.pdf");

                break;
            case 2: // Certificado
                path = Paths.get(directoryPath, ruc + "-autorizacion.pdf");
                break;
            case 3:
                path = Paths.get(directoryPathThree, ruc + "-cv.pdf");
                break;
            case 4: // Certificado
                path = Paths.get(directoryPathFour, ruc + ".png");
                break;
            case 5: // Certificado
                path = Paths.get(directoryPathFive, ruc + "-fichaRuc.pdf");
                break;
            default:
                throw new IllegalArgumentException("Tipo de documento no soportado.");
        }

        // Guardar el archivo en la ruta específica
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo guardado en: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
            throw new IOException("No se pudo guardar el archivo en la ubicación: " + path, e);
        }
}

}
