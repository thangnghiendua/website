package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ut.edu.vaccinemanagement.models.Appointment;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.AppointmentStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   List<Appointment> findByUser_UserId(long userId);

   List<Appointment> findByAppointmentDate(Date appointmentDate);

   List<Appointment> findByUser_UserIdAndAppointmentStatus(Long userId, AppointmentStatus appointmentStatus);

   int countByAppointmentStatus(AppointmentStatus status);
   int countByAppointmentDateBetween(Date startDate, Date endDate);

   long countByAppointmentDateAfterAndAppointmentStatus(Date date, AppointmentStatus status);

   @Query("SELECT MONTH(a.appointmentDate), COUNT(a) " +
           "FROM Appointment a WHERE YEAR(a.appointmentDate) = :year " +
           "GROUP BY MONTH(a.appointmentDate)")
   List<Object[]> findMonthlyStats(@Param("year") int year);


   @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId AND a.appointmentDate > CURRENT_TIMESTAMP")
   List<Appointment> findUpcomingAppointmentsByDoctorId(Long doctorId);

   List<Appointment> findByAppointmentStatus(AppointmentStatus status);


   List<Appointment> findByDoctorDoctorIdAndAppointmentStatus(Long doctorId, AppointmentStatus status);


}
