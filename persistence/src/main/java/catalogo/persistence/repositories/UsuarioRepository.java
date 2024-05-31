package catalogo.persistence.repositories;

import catalogo.persistence.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByRuc(Long ruc);
    Usuario findByCodUsuario(Integer codUsuario);

    @Query("SELECT CAST(COUNT(u) AS INTEGER) FROM Usuario u WHERE u.tipoUsuario.id = 3")
    Long countUsuariosByTipo();
}
