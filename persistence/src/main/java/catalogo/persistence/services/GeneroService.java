package catalogo.persistence.services;

import catalogo.persistence.models.Genero;
import catalogo.persistence.repositories.GeneroRepository;
import org.springframework.stereotype.Service;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public Genero getGeneroById(Integer id){
        return generoRepository.findGeneroById(id);
    }
}
