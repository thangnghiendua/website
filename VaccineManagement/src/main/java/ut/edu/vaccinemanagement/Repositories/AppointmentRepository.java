package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ut.edu.vaccinemanagement.models.Appointment;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.AppointmentStatus;

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

   int countByAppointmentStatus(AppointmentStatus status);
   int countByAppointmentDateBetween(Date startDate, Date endDate);

   long countByAppointmentDateAfterAndAppointmentStatus(Date date, AppointmentStatus status);

   @Query("SELECT MONTH(a.appointmentDate), COUNT(a) " +
           "FROM Appointment a WHERE YEAR(a.appointmentDate) = :year " +
           "GROUP BY MONTH(a.appointmentDate)")
   List<Object[]> findMonthlyStats(@Param("year") int year);
}
