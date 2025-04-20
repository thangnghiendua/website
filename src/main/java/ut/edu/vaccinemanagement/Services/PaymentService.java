package ut.edu.vaccinemanagement.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.DTOs.CreatePaymentRequest;
import ut.edu.vaccinemanagement.models.*;
import ut.edu.vaccinemanagement.Repositories.AppointmentRepository;
import ut.edu.vaccinemanagement.Repositories.PaymentRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Payment createPayment(CreatePaymentRequest request) throws Exception {
        Optional<User> userOpt = userRepository.findById(request.userId);
        if (userOpt.isEmpty()) throw new Exception("Không tìm thấy người dùng với ID: " + request.userId);

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(request.appointmentId);
        if (appointmentOpt.isEmpty()) throw new Exception("Không tìm thấy cuộc hẹn với ID: " + request.appointmentId);

        Payment payment = new Payment();
        payment.setUser(userOpt.get());
        payment.setAppointment(appointmentOpt.get());
        payment.setAmount(request.amount);
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(request.paymentStatus != null ? request.paymentStatus : PaymentStatus.Are_Trading);
        payment.setDescription(request.description);

        return paymentRepository.save(payment);
    }
}
