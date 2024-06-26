package catalogo.persistence.repositories;

import catalogo.persistence.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiciosRepository  extends JpaRepository<Servicio, Integer> {
    Servicio findSectorById(Integer id);

    @Query("SELECT COUNT(U) FROM Servicio U")
    Integer countServicios();
}
