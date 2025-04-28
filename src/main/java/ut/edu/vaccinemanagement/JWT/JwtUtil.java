package ut.edu.vaccinemanagement.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.util.Date;

@Component
public class JwtUtil {


    private final SecretKey key = Keys.hmacShaKeyFor("your-256-bit-secret-key-here-must-be-at-least-32-bytes-long".getBytes(StandardCharsets.UTF_8));



    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)

                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }


    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)

                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }




    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }



    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)


                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
