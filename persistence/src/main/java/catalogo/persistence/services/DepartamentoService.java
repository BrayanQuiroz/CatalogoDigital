package catalogo.persistence.services;

import catalogo.persistence.models.Departamento;
import catalogo.persistence.repositories.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento getDepartamentoById(Integer codDep) {
        return departamentoRepository.findDepartamentoByCodDep(codDep);
    }
}
