package catalogo.persistence.repositories;

import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica, Long> {
    PersonaJuridica findByUsuarioCodUsuario(Integer codUsuario);
    Optional<PersonaJuridica> findByRuc(Long ruc);

//    PersonaJuridica findByRUC(Usuario usuario);
    PersonaJuridica findByUsuarioAndFlagEstadoAndFlagUpdate(Usuario usuario, Short flagEstado, Short flagUpdate);

    List<PersonaJuridica> findAllByFlagEstadoAndFlagUpdate(Short flagEstado, Short flagUpdate);
    List<PersonaJuridica> findAllByUsuarioCodUsuarioAndFlagEstadoAndFlagUpdate(Integer codUsuario, Short flagEstado, Short flagUpdate);
}
