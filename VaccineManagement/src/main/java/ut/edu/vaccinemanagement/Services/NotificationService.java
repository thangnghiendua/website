package ut.edu.vaccinemanagement.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.Repositories.AppointmentRepository;
import ut.edu.vaccinemanagement.Repositories.NotificationRepository;
import ut.edu.vaccinemanagement.models.Appointment;
import ut.edu.vaccinemanagement.models.Notification;
import ut.edu.vaccinemanagement.models.NotificationStatus;
import ut.edu.vaccinemanagement.models.NotificationType;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

@Service
public class NotificationService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendAppointmentReminders() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        List<Appointment> upcomingAppointments = appointmentRepository.findByAppointmentDate(tomorrow);

        for (Appointment appointment : upcomingAppointments) {
            Notification notification = new Notification();
            notification.setUser(appointment.getUser());
            notification.setAppointment(appointment);
            notification.setMessage("Nhắc nhở: Bạn có lịch hẹn tiêm vào ngày mai!");
            notification.setNotificationStatus(NotificationStatus.have_not_read_yet);
            notification.setNotificationType(NotificationType.Reminder);
            notification.setNotificationDate(new Date());

            notificationRepository.save(notification);
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}