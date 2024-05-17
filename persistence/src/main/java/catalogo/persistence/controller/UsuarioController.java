package catalogo.persistence.controller;

import catalogo.persistence.dto.DatosDTO;
import catalogo.persistence.dto.PersonaJuridicaDTO;
import catalogo.persistence.dto.UpdateStateDTO;
import catalogo.persistence.dto.UsuarioDTO;
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

    @PostMapping(value = "/registro", consumes = "multipart/form-data")
    public ResponseEntity<?> registrar(
            @RequestPart("usuario") UsuarioDTO usuarioDTO,
            @RequestPart("personaJuridica") PersonaJuridicaDTO personaJuridicaDTO,
            @RequestParam("pdfCV") MultipartFile pdfCV,
            @RequestParam(value = "pdfAutorizacion", required = false) MultipartFile pdfAutorizacion,
            @RequestParam("pdfCertificado") MultipartFile pdfCertificado,
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
                        CadenaProductiva cadenaProductiva = cadenaProductivaService.getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
                        Sector sector = sectorService.getSectorByCodSector(personaJuridica.getServicio().getId());
                        Departamento departamento = departamentoService.getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
                        Servicio servicio = servicioService.getServicioByCodServicio(personaJuridica.getServicio().getId());

                        Map<String, Object> response = new HashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("genero", personaJuridica.getGenero());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
                        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
                        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("sector", sector.getDescripcion());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("departamento", personaJuridica.getDepartamentos());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamentos", personaJuridica.getDistrito());
                        response.put("departamentos", departamento.getDescripcion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
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
        CadenaProductiva cadenaProductiva = cadenaProductivaService.getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
        Sector sector = sectorService.getSectorByCodSector(personaJuridica.getServicio().getId());
        Departamento departamento = departamentoService.getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
        Servicio servicio = servicioService.getServicioByCodServicio(personaJuridica.getServicio().getId());


        Map<String, Object> response = new HashMap<>();
        response.put("codusuario", usuario.getCodUsuario());
        response.put("ruc", usuario.getRuc());
        response.put("password", usuario.getPassword());
        response.put("estado", usuario.getFlagEstado());
        response.put("flagEstado", usuario.getFlagEstado());
        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
        response.put("razonSocial", personaJuridica.getRazonSocial());
        response.put("nombres", personaJuridica.getNombres());
        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
        response.put("genero", personaJuridica.getGenero());
        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
        response.put("certificadoLaboral", personaJuridica.getCertificadoLaboral());
        response.put("curriculum", personaJuridica.getCurriculum());
        response.put("servicioTitulo", servicio.getTitulo());
        response.put("servicioDescrip", servicio.getDescripcion());
        response.put("update", personaJuridica.getFlagUpdate());
        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
        response.put("sector", sector.getDescripcion());
        response.put("direccion", personaJuridica.getDireccion());
        response.put("departamento", personaJuridica.getDepartamentos());
        response.put("provincia", personaJuridica.getProvincia());
        response.put("distrito", personaJuridica.getDistrito());
        response.put("departamentos", personaJuridica.getDistrito());
        response.put("departamentos", departamento.getDescripcion());
        response.put("provincia", personaJuridica.getProvincia());
        response.put("web", personaJuridica.getWebsite());
        response.put("flagestado", personaJuridica.getFlagEstado());
        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
        response.put("pathImagen","https://dcatalogodigital.itp.gob.pe/media/"+usuario.getRuc()+".png");

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


//    @GetMapping("/usuariosPendiente")
//    public ResponseEntity<?> getUsuariosPendiente() {
//        List<Usuario> usuarios = usuarioService.getUsuarioAll();
//
//        usuarios = usuarios.stream()
//                .filter(usuario -> !usuario.getRuc().equals(20131369477L))
//                .filter(usuario -> usuario.getFlagEstado() == 1)
//                .collect(Collectors.toList());
//
//        if (usuarios.isEmpty()) {
//            Map<String, String> error = new HashMap<>();
//            error.put("error", "No se encontraron usuarios");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//        }
//
//        List<Map<String, Object>> responses = usuarios.stream()
//                .flatMap(usuario -> {
//                    List<PersonaJuridica> personaJuridicas = personaJuridicaService
//                            .getActivePJuridicasByCodUsuarioTwo(usuario.getCodUsuario());
//
//                    return personaJuridicas.stream().map(personaJuridica -> {
//                        CadenaProductiva cadenaProductiva = cadenaProductivaService
//                                .getCadenaProductivaByCodCadProd(personaJuridica.getCadenaProductiva().getCodcadprod());
//                        Sector sector = sectorService.getSectorByCodSector(personaJuridica.getServicio().getId());
//                        Departamento departamento = departamentoService.getDepartamentoById(personaJuridica.getDepartamento().getCodDep());
//                        Servicio servicio = servicioService.getServicioByCodServicio(personaJuridica.getServicio().getId());
//
//                        Map<String, Object> response = new HashMap<>();
//                        response.put("ruc", usuario.getRuc());
//                        response.put("correo", usuario.getCorreo());
//                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
//                        response.put("razonSocial", personaJuridica.getRazonSocial());
//                        response.put("nombres", personaJuridica.getNombres());
//                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
//                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
//                        response.put("genero", personaJuridica.getGenero());
//                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
//                        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
//                        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
//                        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
//                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
//                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
//                        response.put("sector", sector.getDescripcion());
//                        response.put("servicioTitulo", servicio.getTitulo());
//                        response.put("servicioDescrip", servicio.getDescripcion());
//                        response.put("direccion", personaJuridica.getDireccion());
//                        response.put("departamento", personaJuridica.getDepartamentos());
//                        response.put("provincia", personaJuridica.getProvincia());
//                        response.put("distrito", personaJuridica.getDistrito());
//                        response.put("departamentos", personaJuridica.getDistrito());
//                        response.put("departamentos", departamento.getDescripcion());
//                        response.put("provincia", personaJuridica.getProvincia());
//                        response.put("flagestado", personaJuridica.getFlagEstado());
//                        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
//                        response.put("web", personaJuridica.getWebsite());
//                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");
//
//                        return response;
//                    });
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(responses);
//    }



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

                        Map<String, Object> response = new HashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("genero", personaJuridica.getGenero());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
                        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
                        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("sector", sector.getDescripcion());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("departamento", personaJuridica.getDepartamentos());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamentos", personaJuridica.getDistrito());
                        response.put("departamentos", departamento.getDescripcion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
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
                .collect(Collectors.toList());

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

                        Map<String, Object> response = new HashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("genero", personaJuridica.getGenero());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
                        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
                        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("sector", sector.getDescripcion());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("departamento", personaJuridica.getDepartamentos());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamentos", personaJuridica.getDistrito());
                        response.put("departamentos", departamento.getDescripcion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
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

                        Map<String, Object> response = new HashMap<>();
                        response.put("ruc", usuario.getRuc());
                        response.put("correo", usuario.getCorreo());
                        response.put("tipoUsuario", usuario.getTipoUsuario().getDesTipoUsu());
                        response.put("razonSocial", personaJuridica.getRazonSocial());
                        response.put("nombres", personaJuridica.getNombres());
                        response.put("apellidoPaterno", personaJuridica.getApellidoPaterno());
                        response.put("apellidoMaterno", personaJuridica.getApellidoMaterno());
                        response.put("genero", personaJuridica.getGenero());
                        response.put("fechaNacimiento", personaJuridica.getFechaNacimiento());
                        response.put("tipoProveedor", personaJuridica.getTipoProveedor());
                        response.put("nivelAcademico", personaJuridica.getNivelAcademico());
                        response.put("carreraProfesional", personaJuridica.getCarreraProfesional());
                        response.put("experienciaLaboral", personaJuridica.getExperienciaLaboral());
                        response.put("cadenaProductiva", cadenaProductiva.getDescadprod());
                        response.put("sector", sector.getDescripcion());
                        response.put("servicioTitulo", servicio.getTitulo());
                        response.put("servicioDescrip", servicio.getDescripcion());
                        response.put("direccion", personaJuridica.getDireccion());
                        response.put("departamento", personaJuridica.getDepartamentos());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("distrito", personaJuridica.getDistrito());
                        response.put("departamentos", personaJuridica.getDistrito());
                        response.put("departamentos", departamento.getDescripcion());
                        response.put("provincia", personaJuridica.getProvincia());
                        response.put("flagestado", personaJuridica.getFlagEstado());
                        response.put("getFlagUpdate", personaJuridica.getFlagUpdate());
                        response.put("web", personaJuridica.getWebsite());
                        response.put("pathImagen", "https://dcatalogodigital.itp.gob.pe/media/" + usuario.getRuc() + ".png");

                        return response;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}


