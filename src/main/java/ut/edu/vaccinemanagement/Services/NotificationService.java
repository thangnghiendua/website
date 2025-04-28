package ut.edu.vaccinemanagement.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.Repositories.AppointmentRepository;
import ut.edu.vaccinemanagement.Repositories.NotificationRepository;
import ut.edu.vaccinemanagement.models.*;
import ut.edu.vaccinemanagement.models.Appointment;

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

        System.out.println("Fetching notifications for user ID: " + userId);
        List<Notification> notifications = notificationRepository.findByUser_UserId(userId);
        System.out.println("Found " + notifications.size() + " notifications");
        return notifications;
    }

    public void sendAppointmentConfirmationNotification(Long userId) {
        System.out.println("Sending confirmation notification for user ID: " + userId);
        
        List<Appointment> appointments = appointmentRepository.findByUser_UserIdAndAppointmentStatus(userId, AppointmentStatus.Confirmed);
        System.out.println("Found " + appointments.size() + " confirmed appointments");

        if (!appointments.isEmpty()) {
            Appointment appointment = appointments.get(0);
            System.out.println("Processing appointment ID: " + appointment.getAppointmentId());

            try {
                Notification notification = new Notification();
                notification.setUser(appointment.getUser());
                notification.setAppointment(appointment);
                notification.setMessage("Cuộc hẹn của bạn đã được xác nhận. Vui lòng kiểm tra thông tin chi tiết.");
                notification.setNotificationStatus(NotificationStatus.have_not_read_yet);
                notification.setNotificationType(NotificationType.Service);
                notification.setNotificationDate(new Date());
                
                Notification savedNotification = notificationRepository.save(notification);
                System.out.println("Notification saved successfully with ID: " + savedNotification.getNotificationId());
            } catch (Exception e) {
                System.err.println("Error saving notification: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No confirmed appointments found for user ID: " + userId);

        }
    }
}