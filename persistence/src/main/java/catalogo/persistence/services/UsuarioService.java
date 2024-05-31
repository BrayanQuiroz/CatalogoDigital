package catalogo.persistence.services;

import catalogo.persistence.models.Usuario;
import catalogo.persistence.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioByUsuario(Integer codUsuario){
        return usuarioRepository.findByCodUsuario(codUsuario);
    }

    public Usuario getUsuarioByRuc(Long ruc) {
        return usuarioRepository.findByRuc(ruc);
    }

    public List<Usuario> getUsuarioAll(){
        return usuarioRepository.findAll();
    }

    @Transactional
    public void saveUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Long obtenerCantidadProveedores(){
        return usuarioRepository.countUsuariosByTipo();
    }

}
