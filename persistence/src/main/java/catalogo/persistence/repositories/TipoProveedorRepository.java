package catalogo.persistence.repositories;

import catalogo.persistence.models.TipoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProveedorRepository extends JpaRepository<TipoProveedor, Integer> {

    TipoProveedor findTipoProveedorByid(Integer id);
}
