package ut.edu.vaccinemanagement.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Services.UserService;

import java.util.Optional;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/authentication")
    public String showLoginPage() {
        return "register";
    }

}
