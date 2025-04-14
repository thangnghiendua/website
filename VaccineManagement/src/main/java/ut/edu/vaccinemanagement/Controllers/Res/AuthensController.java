package ut.edu.vaccinemanagement.Controllers.Res;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ut.edu.vaccinemanagement.DTOs.LoginRequest;
import ut.edu.vaccinemanagement.DTOs.RegisterDTO;
import ut.edu.vaccinemanagement.JWT.JwtUtil;
import ut.edu.vaccinemanagement.Services.UserService;
import ut.edu.vaccinemanagement.DTOs.AuthResponse;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthensController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        String result = userService.registerUser(registerDTO);
        if (result.equals("Đăng ký thành công!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Đăng nhập người dùng và trả về token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Xác thực người dùng
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Lấy thông tin người dùng đã xác thực
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());

            // Lấy role từ UserDetails
            String role = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(","));

            // Tạo token JWT
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);

            // Trả về token cho client
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
        }
    }

    // Kiểm tra token
    @GetMapping("/profile")
    public String getUserProfile(HttpServletRequest request) {
        // Lấy token từ header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Missing or invalid token!";
        }

        // Cắt bỏ "Bearer " để lấy token thực sự
        String token = authHeader.substring(7);

        return "Token: " + token; // Test xem có nhận được token không
    }
}
