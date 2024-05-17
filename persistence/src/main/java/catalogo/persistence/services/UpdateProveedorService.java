package catalogo.persistence.services;

import catalogo.persistence.models.PersonaJuridica;
import catalogo.persistence.models.Usuario;
import catalogo.persistence.repositories.PersonaJuridicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpdateProveedorService  extends PersonaJuridicaService{

    private final PersonaJuridicaRepository personaJuridicaRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public UpdateProveedorService(PersonaJuridicaRepository personaJuridicaRepository, UsuarioService usuarioService) {
        super(personaJuridicaRepository);
        this.personaJuridicaRepository = personaJuridicaRepository;
        this.usuarioService = usuarioService;
    }

    public void updateDatos(Integer codUsuario) {
        List<PersonaJuridica> observadas = getActivePJuridicasByCodUsuario(codUsuario);
    }


    public ResponseEntity<Map<String, String>> getProveedorByRuc(Long ruc){

        Usuario usuario = usuarioService.getUsuarioByRuc(ruc);

        if( usuario == null || usuario.getFlagEstado() != 1){

            Map<String, String> error = new HashMap<>();

            error.put("Erro", "No se encontro el usuario");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        PersonaJuridica personaJuridica = getActivePJuridicaByCodUsuario(usuario);

        if (personaJuridica == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "El usuario no está activo o actualizado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return null;
    }

}
