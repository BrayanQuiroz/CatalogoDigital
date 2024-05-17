package catalogo.persistence.services;

import catalogo.persistence.models.Servicio;
import catalogo.persistence.repositories.ServiciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioService {

    private final  ServiciosRepository serviciosRepository;

    @Autowired
    public ServicioService(ServiciosRepository serviciosRepository) {
        this.serviciosRepository = serviciosRepository;
    }

    public Servicio getServicioByCodServicio(Integer id) {
        return serviciosRepository.findSectorById(id);
    }
}
