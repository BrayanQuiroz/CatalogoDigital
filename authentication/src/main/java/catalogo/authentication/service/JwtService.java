package catalogo.authentication.service;

import catalogo.authentication.dto.UsuarioDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Map;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private static final String SECRET_KEY = "4hhbvSXWy4/wdx1N+Jd5oqBgbg6EzMb9YnJJKgDlyrz/ZoHREhQwnTZYtR7aPTGVvErUvB2XxvcW9Vq5kIBiVw==";
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(UsuarioDTO usuarioDTO) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("ruc", usuarioDTO.getRuc());
        claims.put("apellidoPaterno", usuarioDTO.getApellidoPaterno());
        claims.put("estado", usuarioDTO.getEstado());
        claims.put("fechaNacimiento", usuarioDTO.getFechaNacimiento());
        claims.put("experienciaLaboral", usuarioDTO.getExperienciaLaboral());
        claims.put("departamentos", usuarioDTO.getDepartamentos());
        claims.put("update", usuarioDTO.getUpdate());
        claims.put("provincia", usuarioDTO.getProvincia());
        claims.put("nombres", usuarioDTO.getNombres());
        claims.put("apellidoMaterno", usuarioDTO.getApellidoMaterno());
        claims.put("carreraProfesional", usuarioDTO.getCarreraProfesional());
        claims.put("razonSocial", usuarioDTO.getRazonSocial());
        claims.put("web", usuarioDTO.getWeb());
        claims.put("genero", usuarioDTO.getGenero());
        claims.put("nivelAcademico", usuarioDTO.getNivelAcademico());
        claims.put("cadenaProductiva", usuarioDTO.getCadenaProductiva());
        claims.put("sector", usuarioDTO.getSector());
        claims.put("certificadoLaboral", usuarioDTO.getCertificadoLaboral());
        claims.put("distrito", usuarioDTO.getDistrito());
        claims.put("direccion", usuarioDTO.getDireccion());
        claims.put("codusuario", usuarioDTO.getCodusuario());
        claims.put("curriculum", usuarioDTO.getCurriculum());
        claims.put("flagEstado", usuarioDTO.getFlagEstado());
        claims.put("tipoProveedor", usuarioDTO.getTipoProveedor());
        claims.put("departamento", usuarioDTO.getDepartamento());
        claims.put("tipoUsuario", usuarioDTO.getTipoUsuario());

        try {
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
            SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

            String jwt = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(signingKey, SignatureAlgorithm.HS512)
                    .compact();

            return "{\"token\": \"" + jwt + "\"}";
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null; // or handle the exception as needed
        }
    }
}