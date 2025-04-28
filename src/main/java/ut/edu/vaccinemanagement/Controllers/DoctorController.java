package ut.edu.vaccinemanagement.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    @GetMapping("/dashboard")
    public String showDoctorPage() {
        return "doctor/dashboard";
    }
}
