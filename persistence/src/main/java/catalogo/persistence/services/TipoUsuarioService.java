package catalogo.persistence.services;

import catalogo.persistence.models.TipoUsuario;
import catalogo.persistence.repositories.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public TipoUsuario getTipoUsuarioById(Integer id) {
        return tipoUsuarioRepository.findTipoUsuarioById(id);
    }
}
