package catalogo.persistence.repositories;

import catalogo.persistence.models.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    Departamento findDepartamentoByCodDep(Integer codDep);

    @Query("SELECT CAST(COUNT(u) AS INTEGER) FROM Departamento u")
    Integer countDepartamentoByTipo();
}
