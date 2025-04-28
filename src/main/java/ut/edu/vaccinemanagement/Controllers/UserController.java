package ut.edu.vaccinemanagement.Controllers;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "user/dashboard";
    }

    @GetMapping("/appointments")
    public String showAppointments() {
        return "user/appointments";
    }

    @GetMapping("/notifications")
    public String showNotifications() {
        return "user/notifications";
    }

    @GetMapping("/children")
    public String showChildren(Model model) {
        return "user/children";
    }

    @GetMapping("/vaccines")
    public String showVaccines() {
        return "user/vaccines";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }


    @GetMapping("/feedback")
    public String showFeedback() {
        return "user/feedback";
    }

    @GetMapping("/symptom-report")
    public String showSymptomReport() {
        return "user/symptom-report";
    }

}
