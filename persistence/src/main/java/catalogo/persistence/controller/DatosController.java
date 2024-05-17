package catalogo.persistence.controller;


import catalogo.persistence.dto.DatosDTO;
import catalogo.persistence.services.DatosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save")
public class DatosController {


    private static final Logger logger = LoggerFactory.getLogger(DatosController.class);


    private final  DatosService datosService;

    @Autowired
    public DatosController(DatosService datosService) {
        this.datosService = datosService;
    }


    @PostMapping("/data")
    public ResponseEntity<String> guardarDatos(@RequestBody DatosDTO[] datosArray) {
        try {
            // Crear una instancia de ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            for (DatosDTO datos : datosArray) {
                String jsonString = objectMapper.writeValueAsString(datos);
                System.out.println("Datos recibidos: " + jsonString);
                datosService.guardarDatos(datos);
            }

            return ResponseEntity.ok("Datos guardados exitosamente");
        } catch (Exception e) {
            logger.error("Error al guardar los datos: ", e);
            return ResponseEntity.internalServerError().body("Error al guardar los datos: " + e.getMessage());
        }
    }
}
