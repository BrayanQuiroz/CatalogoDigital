package catalogo.persistence.services;

import catalogo.persistence.models.Especialidad;
import catalogo.persistence.repositories.EspecialidadRepository;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadServicie {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadServicie(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    public Especialidad getEspecialidad(Integer id) {
        return especialidadRepository.findEspecialidadById(id);
    }
}
