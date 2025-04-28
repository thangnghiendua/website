package ut.edu.vaccinemanagement.Controllers.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ut.edu.vaccinemanagement.DTOs.VaccineUpdateDTO;
import ut.edu.vaccinemanagement.Services.DashBoardService;
import ut.edu.vaccinemanagement.Services.UserService;
import ut.edu.vaccinemanagement.Services.VaccineService;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.models.UserRole;
import ut.edu.vaccinemanagement.models.Vaccine;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminsController {

    private final DashBoardService dashBoardService;
    private final UserService userService;
    private final VaccineService vaccineService;
    @Autowired
    public AdminsController(DashBoardService dashBoardService, UserService userService, VaccineService vaccineService) {
        this.dashBoardService = dashBoardService;
        this.userService = userService;
        this.vaccineService = vaccineService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardSummary() {
        return dashBoardService.getDashboardSummary();
    }

    @GetMapping("/children-by-gender")
    public Map<String, Long> getChildrenByGender() {
        return dashBoardService.getChildrenByGender();
    }

    @GetMapping("/children-by-age-group")
    public Map<String, Long> getChildrenByAgeGroup() {
        return dashBoardService.getChildrenByAgeGroup();
    }

    @GetMapping("/monthly-appointments")
    public List<Map<String, Object>> getMonthlyAppointments(@RequestParam int year) {
        return dashBoardService.getMonthlyAppointments(year);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping("/add/users")

    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PatchMapping("/users/{id}")
    public User updateUserPartial(@PathVariable Long id,
                                  @RequestParam(required = false) String userName,
                                  @RequestParam(required = false) String userRole,
                                  @RequestParam(required = false) String gender,
                                  @RequestParam(required = false) String birthDate,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String password) {
        return userService.updateUserPartial(id, userName, userRole, gender, birthDate, email, password); // Call service to update user
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/users/role")

    public List<User> getUsersByRole(@RequestParam String userRole) {
        try {
            UserRole role = UserRole.valueOf(userRole.toUpperCase());
            return userService.getUsersByRole(role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + userRole);
        }

    }

    @GetMapping("/vaccines")
    public List<Vaccine> getAllVaccine() {
        return vaccineService.getAllVaccine();
    }


    @PatchMapping("/vaccines/{id}")
    public Vaccine updateVaccine(@PathVariable Long id, @RequestBody VaccineUpdateDTO dto) {
        return vaccineService.updateVaccine(id, dto);
    }

}
