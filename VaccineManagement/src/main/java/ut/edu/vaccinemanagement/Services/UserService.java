package ut.edu.vaccinemanagement.Services;
import org.springframework.security.crypto.password.PasswordEncoder;
import ut.edu.vaccinemanagement.models.Gender;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;
import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserPartial(Long id, String fullName, String userRole, String gender, String birthDate, String email, String password) {
        return userRepository.findById(id).map(user -> {
            if (fullName != null && !fullName.isEmpty()) {
                user.setFullName(fullName);
            }
            if (userRole != null) {
                user.setUserRole(UserRole.valueOf(userRole.toUpperCase()));
            }
            if (gender != null) {
                user.setGender(Gender.valueOf(gender.toUpperCase()));
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
}
