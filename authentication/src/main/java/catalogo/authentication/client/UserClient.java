package catalogo.authentication.client;

import catalogo.authentication.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "persistence", url = "${persistence.url}")
public interface UserClient {

    @GetMapping("/users/login/{ruc}")
    UsuarioDTO findByUsername(@PathVariable("ruc") Long ruc)  ;
}
 