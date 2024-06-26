package catalogo.persistence.repositories;

import catalogo.persistence.models.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {
        TipoUsuario findTipoUsuarioById(Integer id);
}
