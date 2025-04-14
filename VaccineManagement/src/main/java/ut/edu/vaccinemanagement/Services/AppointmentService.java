package ut.edu.vaccinemanagement.Services;
import jakarta.transaction.Transactional;
import ut.edu.vaccinemanagement.Repositories.AppointmentRepository;
import ut.edu.vaccinemanagement.Repositories.ChildRepository;
import ut.edu.vaccinemanagement.Repositories.VaccineRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import ut.edu.vaccinemanagement.Repositories.DoctorRepository;
import ut.edu.vaccinemanagement.models.Appointment;
import ut.edu.vaccinemanagement.models.AppointmentStatus;
import ut.edu.vaccinemanagement.models.Child;
import ut.edu.vaccinemanagement.models.Vaccine;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ChildRepository childRepository;

    @Autowired
    VaccineRepository vaccineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NotificationService notificationService;

    public Appointment bookAppointment(Long userId, Long childId, Long vaccineId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ"));

        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vaccine"));


        Doctor doctor = doctorRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));


        String appointmentAddress = "Phòng tiêm mặc định";
        String roomNumber = "Phòng 1";

        Date appointmentDate = new Date();

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setChild(child);
        appointment.setDoctor(doctor);
        appointment.setVaccine(vaccine);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentAddress(appointmentAddress);
        appointment.setRoomNumber(roomNumber);
        appointment.setAppointmentStatus(AppointmentStatus.Pending);

        // Lưu cuộc hẹn
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return savedAppointment;
    }

    public Appointment confrimedAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch tiêm"));

        appointment.setAppointmentStatus(AppointmentStatus.Confirmed);

        // Gửi thông báo xác nhận
        notificationService.sendAppointmentConfirmationNotification(appointment.getUser().getUserId());

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn!"));

        if (appointment.getAppointmentStatus() == AppointmentStatus.Canceled) {
            throw new IllegalStateException("Cuộc hẹn đã được hủy trước đó.");
        }
        if (appointment.getAppointmentDate().before(new Date())) {
            throw new IllegalStateException("Không thể hủy cuộc hẹn đã diễn ra.");
        }
        appointment.setAppointmentStatus(AppointmentStatus.Canceled);

        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch tiêm"));
        appointment.setAppointmentStatus(AppointmentStatus.Complete);
        return appointmentRepository.save(appointment);
    }

<<<<<<< HEAD
    public List<Appointment> appointmentHistoryOfUser (Appointment appointment){
        return appointmentRepository.findByUserId(appointment.getUser().getUserId());
=======
    public List<Appointment> appointmentHistoryOfUser(Long userId) {
        return appointmentRepository.findByUser_UserId(userId);
>>>>>>> fe8c180 (Update Project)
    }


}

