package catalogo.persistence.controller;

import catalogo.persistence.dto.*;
import catalogo.persistence.repositories.TipoPersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import catalogo.persistence.models.*;
import catalogo.persistence.repositories.CadenaProductivaRepository;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import catalogo.persistence.repositories.SectorRepository;
import catalogo.persistence.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PersonaJuridicaService personaJuridicaService;
    @Autowired
    private UpdateStateService updateStateService;


    private final UpdateProveedorService updateProveedorService;

    @Autowired
    private CadenaProductivaService cadenaProductivaService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private ServicioService servicioService;


    @Autowired
    private RegistroService registroService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;
    @Autowired
    private GeneroService generoService;
    @Autowired
    private TipoPersonService tipoPersonService;
    @Autowired
    private TipoProveedorService tipoProveedorService;
    @Autowired
    private NivelAcademicoService nivelAcademicoService;
    @Autowired
    private EspecialidadServicie especialidadServicie;

    public UsuarioController(UpdateProveedorService updateProveedorService) {
        this.updateProveedorService = updateProveedorService;
    }

    @PostMapping(value = "/registro", consumes = "multipart/form-data")
    public ResponseEntity<?> registrar(
            @RequestPart("usuario") UsuarioDTO usuarioDTO,
            @RequestPart("personaJuridica") PersonaJuridicaDTO personaJuridicaDTO,
            @RequestParam(name = "pdfCV", required = false) MultipartFile pdfCV,
            @RequestParam(value = "pdfAutorizacion", required = false) MultipartFile pdfAutorizacion,
            @RequestParam( value = "pdfCertificado", required = false) MultipartFile pdfCertificado,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam(value ="pdfFichaRuc", required = false) MultipartFile pdfFichaRuc
    ) throws Exception {
        registroService.registrarProveedor(usuarioDTO, personaJuridicaDTO, pdfCV, pdfAutorizacion,
                pdfCertificado, imagen,pdfFichaRuc);
        return ResponseEntity.ok().body("Registro completado con éxito.");
    }


    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios() {
        List<Usuario> usuarios = usuarioService.getUsuarioAll();

        usuarios = usuarios.stream()
                .filter(usuario -> !usuario.getRuc().equals(20131369477L))
                .filter(usuario -> usuario.getFlagEstado() == 1)
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontraron usuarios");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        List<Map<String, Object>> responses = usuarios.stream()
                .flatMap(usuario -> {
                    List<PersonaJuridica> personaJuridicas = personaJuridicaService.getActivePJuridicasByCodUsuario(usuario.getCodUsuario());

                    return personaJuridicas.stream().map(personaJuridica -> {
                        CadenaProductiva cadenaProductiva = cadenaProductivaService
                                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
                        Genero genero = generoService.getGeneroById(personaJuridica.getGenero().getId());
                        TipoPersona tipoPersona = tipoPersonService.getTipoPersona(personaJuridica.getTipoPersona().getId());
                        TipoProveedor proveedor = tipoProveedorService.getTipoProveedor(personaJuridica.getTipoProveedor().getId());
                        NivelAcademico nivelAcademico = nivelAcademicoService.getNivelAcademico(personaJuridica.getTipoPersona().getId());
                        Sector sector = sectorService.getSectorByCodSector(personaJuridica.getServicio().getId());
                        Departamento departamento = departamentoService.getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
                        Servicio servicio = servicioService.getServicioByCodServicio(personaJuridica.getServicio().getId());

                        Especialidad especialidad = especialidadServicie.getEspecialidad(personaJuridica.getEspecialidad().getId());

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("idGenero", genero.getId());
                        response.put("genero",  genero.getDescripcion());
                        response.put("idTipoPersona",  tipoPersona.getId());
                        response.put("TipoPersona",  tipoPersona.getDescripcion());
                        response.put("idTipoProveedor",  proveedor.getId());
                        response.put("TipoProveedor",  proveedor.getDescripcion());
                        response.put("idNivelAcademico",  nivelAcademico.getId());
                        response.put("nivelAcademico",  nivelAcademico.getDescripcion());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("idCadenaProductiva", cadenaProductiva.getCodcadprod());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("idEspecialidad", especialidad.getId());
                        response.put("especialidad", especialidad.getDescripcion());
                        response.put("idSector", sector.getCodSector());
                        response.put("sector", sector.getDescripcion());
                        response.put("idServicio", servicio.getId());
                        response.put("flagimagen",personaJuridica.getFlagimagen());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamento", departamento.getDescripcion());
                        response.put("idDepartamento", departamento.getCodDep());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("flagUpdate", personaJuridica.getFlagUpdate());
                        response.put("web", personaJuridica.getWebsite());
                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

                        return response;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{ruc}")
    public ResponseEntity<?> getUsuarioByRuc(@PathVariable Long ruc) {
        Usuario usuario = usuarioService.getUsuarioByRuc(ruc);

        if (usuario == null || usuario.getFlagEstado() != 1) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontró el usuario o el usuario no está activo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        PersonaJuridica personaJuridica = personaJuridicaService.getActivePJuridicaByCodUsuario(usuario);

        if (personaJuridica == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El usuario no está activo o actualizado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        CadenaProductiva cadenaProductiva = cadenaProductivaService
                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
        Genero genero = generoService.getGeneroById(personaJuridica.getGenero().getId());
        TipoPersona tipoPersona = tipoPersonService
                .getTipoPersona(personaJuridica.getTipoPersona().getId());
        TipoProveedor proveedor = tipoProveedorService
                .getTipoProveedor(personaJuridica.getTipoProveedor().getId());
        NivelAcademico nivelAcademico = nivelAcademicoService
                .getNivelAcademico(personaJuridica.getNivelAcademico().getId());
        Sector sector = sectorService
                .getSectorByCodSector(personaJuridica.getSector().getCodSector());
        Departamento departamento = departamentoService
                .getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
        Servicio servicio = servicioService.getServicioByCodServicio(personaJuridica.getServicio().getId());

        Especialidad especialidad = especialidadServicie.getEspecialidad(personaJuridica.getEspecialidad().getId());


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ruc", usuario.getRuc());
        response.put("correo", usuario.getCorreo());
        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
        response.put("razonSocial", personaJuridica.getRazonSocial());
        response.put("nombres", personaJuridica.getNombres());
        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
        response.put("idGenero", genero.getId());
        response.put("password", usuario.getPassword());
        response.put("genero",  genero.getDescripcion());
        response.put("idTipoPersona",  tipoPersona.getId());
        response.put("TipoPersona",  tipoPersona.getDescripcion());
        response.put("idTipoProveedor",  proveedor.getId());
        response.put("TipoProveedor",  proveedor.getDescripcion());
        response.put("idNivelAcademico",  nivelAcademico.getId());
        response.put("nivelAcademico",  nivelAcademico.getDescripcion());
        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
        response.put("idCadenaProductiva", cadenaProductiva.getCodcadprod());
        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
        response.put("flagimagen",personaJuridica.getFlagimagen());
        response.put("idEspecialidad", especialidad.getId());
        response.put("especialidad", especialidad.getDescripcion());
        response.put("idSector", sector.getCodSector());
        response.put("sector", sector.getDescripcion());
        response.put("idServicio", servicio.getId());
        response.put("servicioTitulo", servicio.getTitulo());
        response.put("servicioDescrip", servicio.getDescripcion());
        response.put("direccion", personaJuridica.getDireccion());
        response.put("provincia", personaJuridica.getProvincia());
        response.put("distrito", personaJuridica.getDistrito());
        response.put("departamento", departamento.getDescripcion());
        response.put("idDepartamento", departamento.getCodDep());
        response.put("flagestado", personaJuridica.getFlagEstado());
        response.put("flagUpdate", personaJuridica.getFlagUpdate());
        response.put("web", personaJuridica.getWebsite());
        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

        return ResponseEntity.ok(response);

    }

    @PutMapping("/ActualizarEstado")
    public ResponseEntity<?> updateState(@RequestBody UpdateStateDTO updateStateDTO) {

        log.info("Recibido DTO: {}", updateStateDTO);
        log.info("RUC: {}", updateStateDTO.getRuc());
        log.info("FlagUpdate: {}", updateStateDTO.getFlagUpdate());
        log.info("Usumod: {}", updateStateDTO.getUsumod());
        try {
            Long ruc = updateStateDTO.getRuc();
            Short flagUpdate = updateStateDTO.getFlagUpdate();
            String usumod = updateStateDTO.getUsumod();

            PersonaJuridica personaJuridica = updateStateService.updateEstado(ruc, flagUpdate, usumod);
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } catch (EntityNotFoundException e) {
            log.error("Error al actualizar el estado de la persona jurídica", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Persona jurídica no encontrada con el RUC: " + updateStateDTO.getRuc());
        } catch (Exception e) {
            log.error("Error al actualizar el estado de la persona jurídica", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado de la persona jurídica: " + e.getMessage());
        }
    }


    @GetMapping("/usuariosPendiente")
    public ResponseEntity<?> getUsuarioPendiente() {
        List<Usuario> usuarios = usuarioService.getUsuarioAll();

        usuarios = usuarios.stream()
                .filter(usuario -> !usuario.getRuc().equals(20131369477L))
                .filter(usuario -> usuario.getFlagEstado() == 1)
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontraron usuarios");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        List<Map<String, Object>> responses = usuarios.stream()
                .flatMap(usuario -> {
                    List<PersonaJuridica> personaJuridicas = personaJuridicaService
                            .getActivePJuridicasByCodUsuarioTwo(usuario.getCodUsuario());

                    return personaJuridicas.stream().map(personaJuridica -> {
                        CadenaProductiva cadenaProductiva = cadenaProductivaService
                                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
                        Sector sector = sectorService
                                .getSectorByCodSector(personaJuridica.getServicio().getId());
                        Departamento departamento = departamentoService
                                .getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
                        Servicio servicio = servicioService
                                .getServicioByCodServicio(personaJuridica.getServicio().getId());

                        Genero genero = generoService.getGeneroById(personaJuridica.getGenero().getId());
                        TipoPersona tipoPersona = tipoPersonService.getTipoPersona(personaJuridica.getTipoPersona().getId());
                        TipoProveedor proveedor = tipoProveedorService.getTipoProveedor(personaJuridica.getTipoProveedor().getId());
                        NivelAcademico nivelAcademico = nivelAcademicoService.getNivelAcademico(personaJuridica.getTipoPersona().getId());

                        Especialidad especialidad = especialidadServicie.getEspecialidad(personaJuridica.getEspecialidad().getId());

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("idGenero", genero.getId());
                        response.put("genero",  genero.getDescripcion());
                        response.put("idTipoPersona",  tipoPersona.getId());
                        response.put("TipoPersona",  tipoPersona.getDescripcion());
                        response.put("idTipoProveedor",  proveedor.getId());
                        response.put("TipoProveedor",  proveedor.getDescripcion());
                        response.put("idNivelAcademico",  nivelAcademico.getId());
                        response.put("nivelAcademico",  nivelAcademico.getDescripcion());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("idCadenaProductiva", cadenaProductiva.getCodcadprod());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("idEspecialidad", especialidad.getId());
                        response.put("especialidad", especialidad.getDescripcion());
                        response.put("idSector", sector.getCodSector());
                        response.put("sector", sector.getDescripcion());
                        response.put("flagimagen",personaJuridica.getFlagimagen());
                        response.put("idServicio", servicio.getId());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamento", departamento.getDescripcion());
                        response.put("idDepartamento", departamento.getCodDep());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("flagUpdate", personaJuridica.getFlagUpdate());
                        response.put("web", personaJuridica.getWebsite());
                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

                        return response;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/usuariosObservados")
    public ResponseEntity<?> getUsuarioObservados() {
        List<Usuario> usuarios = usuarioService.getUsuarioAll();

        usuarios = usuarios.stream()
                .filter(usuario -> !usuario.getRuc().equals(20131369477L))
                .filter(usuario -> usuario.getFlagEstado() == 1)
                .toList();

        if (usuarios.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontraron usuarios");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        List<Map<String, Object>> responses = usuarios.stream()
                .flatMap(usuario -> {
                    List<PersonaJuridica> personaJuridicas = personaJuridicaService
                            .getActivePJuridicasByCodUsuarioThree(usuario.getCodUsuario());

                    return personaJuridicas.stream().map(personaJuridica -> {
                        CadenaProductiva cadenaProductiva = cadenaProductivaService
                                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
                        Sector sector = sectorService
                                .getSectorByCodSector(personaJuridica.getServicio().getId());
                        Departamento departamento = departamentoService
                                .getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
                        Servicio servicio = servicioService
                                .getServicioByCodServicio(personaJuridica.getServicio().getId());

                        Genero genero = generoService.getGeneroById(personaJuridica.getGenero().getId());
                        TipoPersona tipoPersona = tipoPersonService.getTipoPersona(personaJuridica.getTipoPersona().getId());
                        TipoProveedor proveedor = tipoProveedorService.getTipoProveedor(personaJuridica.getTipoProveedor().getId());
                        NivelAcademico nivelAcademico = nivelAcademicoService
                                .getNivelAcademico(personaJuridica.getTipoPersona().getId());

                        Especialidad especialidad = especialidadServicie
                                .getEspecialidad(personaJuridica.getEspecialidad().getId());

                        Map<String, Object> response = new HashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("idGenero", genero.getId());
                        response.put("genero",  genero.getDescripcion());
                        response.put("idTipoPersona",  tipoPersona.getId());
                        response.put("TipoPersona",  tipoPersona.getDescripcion());
                        response.put("idTipoProveedor",  proveedor.getId());
                        response.put("TipoProveedor",  proveedor.getDescripcion());
                        response.put("idNivelAcademico",  nivelAcademico.getId());
                        response.put("nivelAcademico",  nivelAcademico.getDescripcion());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("idCadenaProductiva", cadenaProductiva.getCodcadprod());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("idEspecialidad", especialidad.getId());
                        response.put("especialidad", especialidad.getDescripcion());
                        response.put("idSector", sector.getCodSector());
                        response.put("sector", sector.getDescripcion());
                        response.put("flagimagen",personaJuridica.getFlagimagen());
                        response.put("idServicio", servicio.getId());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamento", departamento.getDescripcion());
                        response.put("idDepartamento", departamento.getCodDep());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("flagUpdate", personaJuridica.getFlagUpdate());
                        response.put("web", personaJuridica.getWebsite());
                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

                        return response;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/usuariosRechazado")
    public ResponseEntity<?> getUsuarioRechazado() {
        List<Usuario> usuarios = usuarioService.getUsuarioAll();

        usuarios = usuarios.stream()
                .filter(usuario -> !usuario.getRuc().equals(20131369477L))
                .filter(usuario -> usuario.getFlagEstado() == 1)
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se encontraron usuarios");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        List<Map<String, Object>> responses = usuarios.stream()
                .flatMap(usuario -> {
                    List<PersonaJuridica> personaJuridicas = personaJuridicaService
                            .getActivePJuridicasByCodUsuarioFour(usuario.getCodUsuario());

                    return personaJuridicas.stream().map(personaJuridica -> {
                        CadenaProductiva cadenaProductiva = cadenaProductivaService
                                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
                        Sector sector = sectorService
                                .getSectorByCodSector(personaJuridica.getServicio().getId());
                        Departamento departamento = departamentoService
                                .getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
                        Servicio servicio = servicioService
                                .getServicioByCodServicio(personaJuridica.getServicio().getId());

                        Genero genero = generoService.getGeneroById(personaJuridica.getGenero().getId());
                        TipoPersona tipoPersona = tipoPersonService.getTipoPersona(personaJuridica.getTipoPersona().getId());
                        TipoProveedor proveedor = tipoProveedorService.getTipoProveedor(personaJuridica.getTipoProveedor().getId());
                        NivelAcademico nivelAcademico = nivelAcademicoService
                                .getNivelAcademico(personaJuridica.getTipoPersona().getId());
                        Especialidad especialidad = especialidadServicie
                                .getEspecialidad(personaJuridica.getEspecialidad().getId());

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("idGenero", genero.getId());
                        response.put("genero",  genero.getDescripcion());
                        response.put("idTipoPersona",  tipoPersona.getId());
                        response.put("TipoPersona",  tipoPersona.getDescripcion());
                        response.put("idTipoProveedor",  proveedor.getId());
                        response.put("TipoProveedor",  proveedor.getDescripcion());
                        response.put("idNivelAcademico",  nivelAcademico.getId());
                        response.put("nivelAcademico",  nivelAcademico.getDescripcion());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("idCadenaProductiva", cadenaProductiva.getCodcadprod());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("idEspecialidad", especialidad.getId());
                        response.put("especialidad", especialidad.getDescripcion());
                        response.put("idSector", sector.getCodSector());
                        response.put("sector", sector.getDescripcion());
                        response.put("idServicio", servicio.getId());
                        response.put("flagimagen",personaJuridica.getFlagimagen());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamento", departamento.getDescripcion());
                        response.put("idDepartamento", departamento.getCodDep());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("flagUpdate", personaJuridica.getFlagUpdate());
                        response.put("web", personaJuridica.getWebsite());
                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

                        return response;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


//    @PostMapping("/UpdateProveedor")
//    public ResponseEntity<?> updateDatos( @RequestBody  UpdateProveedorDTO updateProveedorDTO requestBody){
//        try{
//
//            Long ruc = requestBody.getRuc();
//            UpdateProveedorDTO updateProveedorDTO = requestBody.getUpdateProveedorDTO();
//
//            updateProveedorService.getProveedorByRuc(ruc, updateProveedorDTO);
//            return ResponseEntity.ok("Datos guardados exitosamente");
//
//        }catch (Exception e){
//            throw new RuntimeException("Error",e);
//        }
//    }

}


