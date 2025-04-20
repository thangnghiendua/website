package ut.edu.vaccinemanagement.Services;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ut.edu.vaccinemanagement.DTOs.LoginRequest;
import ut.edu.vaccinemanagement.DTOs.RegisterDTO;
import ut.edu.vaccinemanagement.DTOs.UpdateUserProfileDTO;
import ut.edu.vaccinemanagement.models.Gender;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.UserRole;
import ut.edu.vaccinemanagement.JWT.JwtUtil;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.List;
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByEmail(username);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getUserPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()))
        );
    }

    public String registerUser(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }

        User user = new User();
        user.setFullName(registerDTO.getUserName());
        user.setGender(registerDTO.getGender());
        user.setBirthDate(registerDTO.getBirthDate());
        user.setEmail(registerDTO.getEmail());
        user.setUserPassword(passwordEncoder.encode(registerDTO.getUserPassword()));
        user.setUserRole(UserRole.User);

        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    public String loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
                return jwtUtil.generateToken(user.getEmail(), user.getUserRole().name());
            }
        }
        throw new RuntimeException("Sai tài khoản hoặc mật khẩu!");
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserPartial(Long id, String userName, String userRole, String gender, String birthDate, String email, String password) {
        return userRepository.findById(id).map(user -> {
            if (userName != null && !userName.isEmpty()) {
                user.setFullName(userName);
            }
            if (userRole != null) {
                try {
                    user.setUserRole(UserRole.valueOf(userRole.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Loại vai trò không hợp lệ!");
                }
            }
            if (gender != null) {
                try {
                    user.setGender(Gender.valueOf(gender.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Giới tính không hợp lệ: " + gender);
                }
            }
            if (birthDate != null) {
                try {
                    Date date = java.sql.Date.valueOf(birthDate);
                    user.setBirthDate(date);
                } catch (Exception e) {
                    throw new RuntimeException("Ngày sinh không hợp lệ");
                }
            }
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            if (password != null && !password.isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(password));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + id));
    }

    public User updateUserProfile(Long userId, UpdateUserProfileDTO dto) {
        return userRepository.findById(userId).map(user -> {
            if (dto.getUserName() != null && !dto.getUserName().isEmpty()) {
                user.setFullName(dto.getUserName());
            }

            if (dto.getGender() != null) {
                try {
                    user.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Giới tính không hợp lệ: " + dto.getGender());
                }
            }

            if (dto.getBirthDate() != null) {
                try {
                    Date date = java.sql.Date.valueOf(dto.getBirthDate());
                    user.setBirthDate(date);
                } catch (Exception e) {
                    throw new RuntimeException("Ngày sinh không hợp lệ");
                }
            }

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy người dùng có id là: " + id);
        }
    }

    public List<User> getUsersByRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}
