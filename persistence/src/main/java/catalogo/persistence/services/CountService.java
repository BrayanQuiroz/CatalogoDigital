package catalogo.persistence.services;

import catalogo.persistence.repositories.CadenaProductivaRepository;
import catalogo.persistence.repositories.DepartamentoRepository;
import catalogo.persistence.repositories.ServiciosRepository;
import catalogo.persistence.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountService {

    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final CadenaProductivaRepository cadenaProductivaRepository;
    private final ServiciosRepository serviciosRepository;

    @Autowired
    public CountService(UsuarioRepository usuarioRepository,
                        DepartamentoRepository departamentoRepository,
                        CadenaProductivaRepository cadenaProductivaRepository,
                        ServiciosRepository serviciosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.departamentoRepository = departamentoRepository;
        this.cadenaProductivaRepository = cadenaProductivaRepository;
        this.serviciosRepository = serviciosRepository;
    }

    public Long obtenerCantidadProveedores(){
        return usuarioRepository.countUsuariosByTipo();
    }

    public Integer obtenerCantidadDeDepartamentos(){
        return departamentoRepository.countDepartamentoByTipo();
    }

    public Integer obtenerCantidadCadenaProductiva(){
        return cadenaProductivaRepository.countCadenaProductiva();
    }

    public Integer obtenerCantidadServicios(){
        return serviciosRepository.countServicios();
    }
}
