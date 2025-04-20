package ut.edu.vaccinemanagement.Controllers.Res;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ut.edu.vaccinemanagement.DTOs.AppointmentRequest;
import ut.edu.vaccinemanagement.DTOs.UpdateUserProfileDTO;
import ut.edu.vaccinemanagement.Services.*;
import ut.edu.vaccinemanagement.models.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final UserChildService userChildService;
    private final ChildService childService;
    private final FeedBackService feedbackService;
    private final ReactionReportService reactionReportService;
    private final WalletService walletService;
    private final VaccineService vaccineService;

    @Autowired
    public UsersController(AppointmentService appointmentService, UserService userService, NotificationService notificationService, UserChildService userChildService, ChildService childService, FeedBackService feedbackService, ReactionReportService reactionReportService, WalletService walletService,VaccineService vaccineService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.userChildService = userChildService;
        this.childService = childService;
        this.feedbackService = feedbackService;
        this.reactionReportService = reactionReportService;
        this.walletService = walletService;
        this.vaccineService = vaccineService;
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(email).getUserId();
    }

    @GetMapping("/notifications")
    public List<Notification> getUserNotifications() {
        Long userId = getCurrentUserId();
        return notificationService.getUserNotifications(userId);
    }

    @PostMapping("/send-confirmation")
    public String sendAppointmentConfirmationNotification() {
        Long userId = getCurrentUserId();
        notificationService.sendAppointmentConfirmationNotification(userId);
        return "Thông báo xác nhận đã được gửi!";
    }
    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequest request) {
        Long userId = getCurrentUserId();

        Appointment appointment = appointmentService.bookAppointment(
                userId,
                request.getChildId(),
                request.getVaccineId()
        );

        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Cuộc hẹn đã được hủy thành công.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/complete/{appointmentId}")
    public ResponseEntity<String> completeAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.completeAppointment(appointmentId);
            return ResponseEntity.ok("Cuộc hẹn đã được đánh dấu là hoàn thành.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/history")
    public List<Appointment> getAppointmentHistory() {
        Long userId = getCurrentUserId();
        return appointmentService.appointmentHistoryOfUser(userId);
    }

    @GetMapping("/children")
    public ResponseEntity<List<Child>> getChildrenOfUser() {
        Long userId = getCurrentUserId();
        List<Child> children = userChildService.getChildrenByUserId(userId);
        return ResponseEntity.ok(children);
    }

    @PostMapping("/children/add")
    public ResponseEntity<Child> addChild(@RequestBody Child child) {
        try {
            Long userId = getCurrentUserId();

            Child savedChild = childService.addChild(child);

            UserChild userChild = new UserChild();
            userChild.setUser(userService.getUserById(userId));
            userChild.setChild(savedChild);
            userChildService.saveUserChild(userChild);

            return ResponseEntity.ok(savedChild);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/children/update/{childId}")
    public ResponseEntity<Child> updateChild(
            @PathVariable Long childId,
            @RequestParam(required = false) String childName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birthDay) {

        try {
            Child updatedChild = childService.updateChildPartial(childId, childName, gender, birthDay);
            return ResponseEntity.ok(updatedChild);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/children/delete/{childId}")
    public ResponseEntity<String> deleteChild(@PathVariable Long childId) {
        try {
            Long userId = getCurrentUserId();

            boolean isOwner = userChildService.isChildOwnedByUser(childId, userId);
            if (!isOwner) {
                return ResponseEntity.status(403).body("Bạn không có quyền xóa trẻ này.");
            }

            userChildService.deleteUserChildRelation(childId, userId);
            childService.deleteChild(childId);

            return ResponseEntity.ok("Xóa trẻ thành công.");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa trẻ: " + e.getMessage());
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> sendFeedback(@RequestBody FeedBack feedback) {
        try {
            Long userId = getCurrentUserId();

            feedback.setUser(userService.getUserById(userId));

            FeedBack savedFeedback = feedbackService.sendFeedback(feedback);
            return ResponseEntity.ok(savedFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/feedback/history")
    public ResponseEntity<List<FeedBack>> getUserFeedbackHistory() {
        Long userId = getCurrentUserId();
        List<FeedBack> feedbacks = feedbackService.getFeedbacksByUserId(userId);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/report-symptoms")
    public ResponseEntity<?> reportSymptoms(@RequestBody ReactionReport report) {
        Long userId = getCurrentUserId();

        ReactionReport savedReport = reactionReportService.reportSymptoms(userId, report);
        if (savedReport != null) {
            return ResponseEntity.ok(savedReport);
        } else {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại.");
        }
    }

    @GetMapping("/reaction-reports")
    public ResponseEntity<List<ReactionReport>> getReactionReportsByUser() {
        Long userId = getCurrentUserId();

        List<ReactionReport> reports = reactionReportService.getReportsByUserId(userId);
        if (reports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/recharge")
    public ResponseEntity<String> rechargeWallet(@RequestParam BigDecimal amount) {
        Long userId = getCurrentUserId();
        boolean success = walletService.rechargeWallet(userId, amount);
        if (success) {
            return ResponseEntity.ok("Nạp tiền thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nạp tiền thất bại");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferAmount(@RequestParam BigDecimal amount) {
        Long userId = getCurrentUserId();
        boolean success = walletService.transferAmount(userId, amount);
        if (success) {
            return ResponseEntity.ok("Chuyển tiền thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chuyển tiền thất bại");
        }
    }

    @GetMapping("/wallet-balance")
    public ResponseEntity<String> getWalletBalance() {
        Long userId = getCurrentUserId();
        BigDecimal balance = walletService.getWalletBalance(userId);
        if (balance != null) {
            return ResponseEntity.ok("Số dư trong ví: " + balance);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ví của người dùng");
        }
    }

    @GetMapping("/vaccines")
    public ResponseEntity<List<Vaccine>> getAllVaccine() {
        List<Vaccine> vaccines = vaccineService.getAllVaccine();
        if (vaccines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(vaccines);
        }
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/by-package-type")
    public ResponseEntity<List<Vaccine>> getVaccinesByPackageType(@RequestParam PackageType packageType) {
        List<Vaccine> vaccines = vaccineService.getAllVaccineByPackageType(packageType);
        if (vaccines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(vaccines);
        }
        return ResponseEntity.ok(vaccines);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<User> updateCurrentUserProfile(@RequestBody UpdateUserProfileDTO dto) {
        Long userId = getCurrentUserId();
        User updatedUser = userService.updateUserProfile(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }
}
