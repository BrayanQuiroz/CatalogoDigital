package catalogo.persistence.services;

import catalogo.persistence.models.NivelAcademico;
import catalogo.persistence.repositories.NivelAcademicoRepository;
import org.springframework.stereotype.Service;

@Service
public class NivelAcademicoService {

    private final NivelAcademicoRepository nivelAcademicoRepository;

    public NivelAcademicoService(NivelAcademicoRepository nivelAcademicoRepository) {
        this.nivelAcademicoRepository = nivelAcademicoRepository;
    }

    public NivelAcademico getNivelAcademico(Integer id) {
        return nivelAcademicoRepository.findNivelAcademicoById(id);
    }
}
