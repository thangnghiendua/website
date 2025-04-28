package ut.edu.vaccinemanagement.Controllers.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ut.edu.vaccinemanagement.DTOs.CreatePaymentRequest;
import ut.edu.vaccinemanagement.Services.PaymentService;
import ut.edu.vaccinemanagement.Services.FeedBackService;
import ut.edu.vaccinemanagement.Services.ReactionReportService;
import ut.edu.vaccinemanagement.models.*;
import ut.edu.vaccinemanagement.Services.AppointmentService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {


    private final AppointmentService appointmentService;
    private final PaymentService paymentService;
    private final FeedBackService feedBackService;
    private final ReactionReportService reactionReportService;

    @Autowired
    public DoctorController(AppointmentService appointmentService, PaymentService paymentService, FeedBackService feedBackService, ReactionReportService reactionReportService) {
        this.appointmentService = appointmentService;
        this.paymentService = paymentService;
        this.feedBackService = feedBackService;
        this.reactionReportService = reactionReportService;
    }


    // 1. Lấy tất cả lịch hẹn sắp tới của bác sĩ
    @GetMapping("/{doctorId}/upcoming-appointments")
    public List<Appointment> getUpcomingAppointments(@PathVariable Long doctorId) {
        return appointmentService.getUpcomingAppointmentsByDoctorId(doctorId);
    }

    // 2. Lấy tất cả các cuộc hẹn đang ở trạng thái Pending
    @GetMapping("/appointments/pending")
    public List<Appointment> getAllPendingAppointments() {
        return appointmentService.getAllPendingAppointments();
    }

    // 3. Cập nhật lịch hẹn nếu trạng thái đang là PENDING
    @PutMapping("/appointments/{appointmentId}")
    public Appointment updateAppointmentIfPending(
            @PathVariable Long appointmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date newDate,
            @RequestParam String newAddress,
            @RequestParam String newRoom,
            @RequestParam AppointmentStatus newStatus
    ) throws Exception {
        return appointmentService.updateAppointmentIfPending(appointmentId, newDate, newAddress, newRoom, newStatus);
    }

    // Chức năng tạo payment cho một cuộc hẹn
    @PostMapping("/payment/create")
    public Payment createPayment(@RequestBody CreatePaymentRequest request) throws Exception {
        return paymentService.createPayment(request);
    }

    // xem các đánh giá
    @GetMapping("/{doctorId}/feedbacks")
    public List<FeedBack> getFeedbacksByDoctorId(@PathVariable long doctorId) {
        return feedBackService.getFeedbacksByDoctorId(doctorId);
    }


    // Lấy báo cáo triệu chứng của bác sĩ trong ngày hôm nay
    @GetMapping("/{doctorId}/reaction-reports/today")
    public List<ReactionReport> getReactionReportsForToday(@PathVariable long doctorId) {
        return reactionReportService.getReactionReportsForToday(doctorId);
    }

    // Cập nhật doctorFeedback cho báo cáo triệu chứng
    @PutMapping("/{doctorId}/reaction-reports/{reactionReportId}")
    public ReactionReport updateDoctorFeedback(@PathVariable long reactionReportId,
                                               @RequestBody String doctorFeedback) throws Exception {
        return reactionReportService.updateDoctorFeedback(reactionReportId, doctorFeedback);
    }

}
