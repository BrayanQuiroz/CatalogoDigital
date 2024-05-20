package catalogo.persistence.services;


import catalogo.persistence.models.TipoProveedor;
import catalogo.persistence.repositories.TipoProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoProveedorService {

    private  final TipoProveedorRepository tipoProveedorRepository;


    public TipoProveedorService(TipoProveedorRepository tipoProveedorRepository) {
        this.tipoProveedorRepository = tipoProveedorRepository;
    }

    public TipoProveedor getTipoProveedor(Integer id) {
        return tipoProveedorRepository.findTipoProveedorByid(id);
    }
}
