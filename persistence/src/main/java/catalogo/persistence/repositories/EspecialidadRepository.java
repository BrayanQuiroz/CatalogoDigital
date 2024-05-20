package catalogo.persistence.repositories;

import catalogo.persistence.models.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    Especialidad findEspecialidadById(Integer id);
}
