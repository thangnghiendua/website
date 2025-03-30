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

    @Transactional
    public Appointment bookAppointment(Long userId, Long childId, Long doctorId, Long vaccineId, Date appointmentDate, String appointmentAddress, String roomNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vaccine"));

        if (appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Ngày hẹn không hợp lệ");
        }

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

    public List<Appointment> appointmentHistoryOfUser (Appointment appointment){
        return appointmentRepository.findByUserId(appointment.getUser().getUserId());
    }


}

