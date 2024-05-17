//package catalogo.authentication.service;
//
//import catalogo.authentication.client.UserClient;
//import catalogo.authentication.config.UserPrincipal;
//import catalogo.authentication.dto.UsuarioDTO;
//import catalogo.authentication.exception.InternalServerErrorException;
//import catalogo.authentication.exception.UserServiceException;
//import feign.FeignException;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//@Service
//public class UsuarioService implements UserDetailsService {
//    private final UserClient userClient;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public UsuarioService(UserClient userClient, BCryptPasswordEncoder passwordEncoder) {
//        this.userClient = userClient;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//
//    public UsuarioDTO getUsuario(Long ruc) {
//        try {
//            UsuarioDTO usuario = userClient.findByUsername(ruc);
//            // Codificar la contraseña con BCrypt
//            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
//            return usuario;
//        } catch (FeignException.FeignClientException ex) {
//            // Maneja adecuadamente las excepciones dependiendo del estado de la respuesta
//            throw new UsernameNotFoundException("No se encontró el usuario con RUC: " + ruc);
//        }
//    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
//            Long ruc = Long.parseLong(username);
//            UsuarioDTO usuario = userClient.findByUsername(ruc);
//            if (usuario == null) {
//                throw new UsernameNotFoundException("Usuario no encontrado con RUC: " + username);
//            }
//            // No encodificar la contraseña aquí
//            return new UserPrincipal(usuario);
//        } catch (NumberFormatException e) {
//            throw new UsernameNotFoundException("Formato de RUC inválido: " + username);
//        }
//    }
//}