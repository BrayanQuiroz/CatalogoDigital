package catalogo.authentication.controller;

import catalogo.authentication.client.UserClient;
import catalogo.authentication.dto.LoginDTO;
import catalogo.authentication.dto.UsuarioDTO;
import catalogo.authentication.service.JwtService;
//import catalogo.authentication.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@RestController
//public class AuthController {
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    public AuthController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticate(@RequestBody UsuarioDTO usuarioDTO) {
//        try {
//            // Usar AuthenticationManager para autenticar al usuario
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(usuarioDTO.getRuc().toString(), usuarioDTO.getPassword())
//            );
//
//            return ResponseEntity.ok("Autenticación exitosa");
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
//        }
//    }
//}

@RequestMapping("/api")
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserClient userClient;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        Long rucLogin = loginDTO.getRuc();
        String passwordLogin = loginDTO.getPassword();
        UsuarioDTO usuarioDTO = userClient.findByUsername(rucLogin);

        System.out.println("Password provided: " + passwordLogin);
        System.out.println("Password stored: " + usuarioDTO.getPassword());

        if (!BCrypt.checkpw(passwordLogin, usuarioDTO.getPassword())){
            System.out.println("Failed login attempt for RUC: " + rucLogin);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Credenciales inválidas\"}");
        }


        String token = jwtService.generateToken(usuarioDTO);
        return ResponseEntity.ok(token);  // Devuelve el token en formato JSON directamente
    }


}