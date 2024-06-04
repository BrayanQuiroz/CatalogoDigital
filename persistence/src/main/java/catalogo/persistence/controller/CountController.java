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
    public Map<String, Integer> countProveedores() {
        Long cantidad = (long) Math.toIntExact(countService.obtenerCantidadProveedores());

        Map<String, Long> respuesta = new HashMap<>();

        
        respuesta.put("proveedores", cantidad);
        return respuesta;

    }
}
