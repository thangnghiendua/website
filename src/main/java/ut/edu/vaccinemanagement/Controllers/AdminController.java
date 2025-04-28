package ut.edu.vaccinemanagement.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }
}

