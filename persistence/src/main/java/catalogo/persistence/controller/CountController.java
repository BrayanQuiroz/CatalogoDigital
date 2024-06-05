package catalogo.persistence.controller;

import catalogo.persistence.services.CountService;
import catalogo.persistence.services.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/count")
public class CountController {

    private final CountService countService;

    public CountController(CountService countService, UsuarioService usuarioService) {
        this.countService = countService;
    }

    @GetMapping("/proveedores")
    public Map<String, Long> countProveedores() {
        Long cantidad = (long) Math.toIntExact(countService.obtenerCantidadProveedores());

        Map<String, Long> respuesta = new HashMap<>();
        respuesta.put("proveedores", cantidad);
        return respuesta;

    }

    @GetMapping("/regiones")
    public Map<String, Integer> countRegiones() {
        Integer cantidad = Math.toIntExact(countService.obtenerCantidadDeDepartamentos());

        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("regiones", cantidad);
        return respuesta;
    }

    @GetMapping("/cadenaProductiva")
    public Map<String, Integer> countCadenaProductiva() {
        Integer cantidad = Math.toIntExact(countService.obtenerCantidadCadenaProductiva());

        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("cadenaProductiva", cantidad);
        return respuesta;
    }

    @GetMapping("/servicios")
    public Map<String, Integer> countServicios() {
        Integer cantidad = Math.toIntExact(countService.obtenerCantidadServicios());

        Map<String, Integer> respuesta = new HashMap<>();
        respuesta.put("servicios", cantidad);
        return respuesta;
    }
}
