package catalogo.persistence.repositories;

import catalogo.persistence.models.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    Departamento findDepartamentoByCodDep(Integer codDep);
}
