package catalogo.persistence.services;

import catalogo.persistence.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class CountService {

    private final UsuarioRepository usuarioRepository;

    public CountService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Long obtenerCantidadProveedores(){
        return usuarioRepository.countUsuariosByTipo();
    }
}
