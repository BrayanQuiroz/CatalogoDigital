//package catalogo.authentication.config;
//
//import catalogo.authentication.dto.UsuarioDTO;
//import java.util.Collections;
//
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//@Data
//public class UserPrincipal implements UserDetails {
//
//    private UsuarioDTO usuario;
//
//    public UserPrincipal(UsuarioDTO usuario) {
//        this.usuario = usuario;
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public String getPassword() {
//        return usuario.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return usuario.getRuc().toString();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
