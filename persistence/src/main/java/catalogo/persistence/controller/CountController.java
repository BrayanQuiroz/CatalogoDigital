package catalogo.persistence.controller;

import catalogo.persistence.services.CountService;
import catalogo.persistence.services.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/count")
public class CountController {

    private final CountService countService;
    private final UsuarioService usuarioService;

    public CountController(CountService countService, UsuarioService usuarioService) {
        this.countService = countService;
        this.usuarioService = usuarioService;
    }

    public Integer CountProveedores(){
        return Math.toIntExact(countService.obtenerCantidadProveedores());
    }
}
