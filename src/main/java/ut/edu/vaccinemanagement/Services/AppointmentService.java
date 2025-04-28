package ut.edu.vaccinemanagement.Services;
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


import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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



        Doctor doctor = doctorRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID 1"));

        String appointmentAddress = "Phòng tiêm mặc định";
        String roomNumber = "Phòng 1";


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date appointmentDate = calendar.getTime();


        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setChild(child);
        appointment.setDoctor(doctor);
        appointment.setVaccine(vaccine);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentAddress(appointmentAddress);
        appointment.setRoomNumber(roomNumber);
        appointment.setAppointmentStatus(AppointmentStatus.Pending);


        return appointmentRepository.save(appointment);

    }


    public Appointment confrimedAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch tiêm"));

        appointment.setAppointmentStatus(AppointmentStatus.Confirmed);

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

    public List<Appointment> appointmentHistoryOfUser(Long userId) {
        return appointmentRepository.findByUser_UserId(userId);
    }

    public List<Appointment> getUpcomingAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findUpcomingAppointmentsByDoctorId(doctorId);
    }


    public List<Appointment> getAllPendingAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorDoctorIdAndAppointmentStatus(doctorId, AppointmentStatus.Pending);

    }

    public Appointment updateAppointmentIfPending(Long appointmentId, Date newDate, String newAddress, String newRoom, AppointmentStatus newStatus) throws Exception {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            throw new Exception("Không tìm thấy lịch hẹn với ID: " + appointmentId);
        }

        Appointment appointment = optionalAppointment.get();

        if (appointment.getAppointmentStatus() != AppointmentStatus.Pending) {
            throw new Exception("Chỉ được phép sửa lịch hẹn có trạng thái PENDING.");
        }

        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentAddress(newAddress);
        appointment.setRoomNumber(newRoom);
        appointment.setAppointmentStatus(newStatus);

        return appointmentRepository.save(appointment);
    }


}

