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
public class DoctorsController {

    private final AppointmentService appointmentService;
    private final PaymentService paymentService;
    private final FeedBackService feedBackService;
    private final ReactionReportService reactionReportService;

    @Autowired
    public DoctorsController(AppointmentService appointmentService, PaymentService paymentService, FeedBackService feedBackService, ReactionReportService reactionReportService) {
        this.appointmentService = appointmentService;
        this.paymentService = paymentService;
        this.feedBackService = feedBackService;
        this.reactionReportService = reactionReportService;
    }

    @GetMapping("/{doctorId}/upcoming-appointments")
    public List<Appointment> getUpcomingAppointments(@PathVariable Long doctorId) {
        return appointmentService.getUpcomingAppointmentsByDoctorId(doctorId);
    }

    @GetMapping("/appointments/pending")
    public List<Appointment> getAllPendingAppointments() {
        return appointmentService.getAllPendingAppointments(1L);
    }

    @PutMapping("/appointments/{appointmentId}")
    public Appointment updateAppointmentIfPending(
            @PathVariable Long appointmentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date newDate,
            @RequestParam String newAddress,
            @RequestParam String newRoom,
            @RequestParam AppointmentStatus newStatus
    ) throws Exception {
        return appointmentService.updateAppointmentIfPending(appointmentId, newDate, newAddress, newRoom, newStatus);
    }

    @PostMapping("/payment/create")
    public Payment createPayment(@RequestBody CreatePaymentRequest request) throws Exception {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{doctorId}/feedbacks")
    public List<FeedBack> getFeedbacksByDoctorId(@PathVariable long doctorId) {
        return feedBackService.getFeedbacksByDoctorId(doctorId);
    }

    @GetMapping("/{doctorId}/reaction-reports/today")
    public List<ReactionReport> getReactionReportsForToday(@PathVariable long doctorId) {
        return reactionReportService.getReactionReportsForToday(doctorId);
    }

    @PutMapping("/{doctorId}/reaction-reports/{reactionReportId}")
    public ReactionReport updateDoctorFeedback(
            @PathVariable long reactionReportId,
            @RequestBody String doctorFeedback) throws Exception {
        return reactionReportService.updateDoctorFeedback(reactionReportId, doctorFeedback);
    }
}
