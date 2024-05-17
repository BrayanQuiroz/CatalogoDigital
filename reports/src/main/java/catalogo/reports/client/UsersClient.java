package catalogo.reports.client;

import catalogo.reports.dto.UsuariosDTO;
import catalogo.reports.dto.UsuariosDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "persistence", url = "${persistence.url}")
public interface UsersClient {
    @GetMapping("/users/usuarios")
    List<UsuariosDTO> getUsuarios();
}
 