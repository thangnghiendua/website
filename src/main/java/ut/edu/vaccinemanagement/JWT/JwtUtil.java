package ut.edu.vaccinemanagement.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // Sử dụng khóa bí mật với độ dài an toàn cho thuật toán HS256
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Phương thức tạo token
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Hết hạn sau 1 giờ
                .signWith(key, SignatureAlgorithm.HS256) // Sử dụng key đã được tạo an toàn cho HS256
                .compact();
    }

    // Lấy username từ token
    public String extractUsername(String token) {
        return Jwts.parserBuilder() // Sử dụng parserBuilder thay vì parser
                .setSigningKey(key) // Dùng key bí mật đã được tạo an toàn
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Kiểm tra tính hợp lệ của token
    public boolean validateToken(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }

    // Lấy role từ token
    public String extractRole(String token) {
        return Jwts.parserBuilder() // Sử dụng parserBuilder thay vì parser
                .setSigningKey(key) // Dùng key bí mật đã được tạo an toàn
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Kiểm tra xem token đã hết hạn hay chưa
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Lấy thông tin Claims từ token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder() // Sử dụng parserBuilder thay vì parser
                .setSigningKey(key) // Dùng key bí mật đã được tạo an toàn
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
