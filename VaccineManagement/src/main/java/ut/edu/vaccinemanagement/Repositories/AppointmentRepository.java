package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.Appointment;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   List <Appointment> findByUserId(long userId);

   List<Appointment> findByChildId(long childId);

   List<Appointment> findByDoctorId(long doctorId);

   Optional<Appointment> findByAppointmentId(long appointmentId);

   List<Appointment> findByAppointmentDate(Date appointmentDate);

}
