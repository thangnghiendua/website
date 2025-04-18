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
        return notificationRepository.findByUser_UserId(userId);
    }

    public void sendAppointmentConfirmationNotification(Long userId) {
        // Truy vấn thông tin các cuộc hẹn
        List<Appointment> appointments = appointmentRepository.findByUser_UserIdAndAppointmentStatus(userId, AppointmentStatus.Pending);

        // Kiểm tra nếu danh sách không rỗng
        if (!appointments.isEmpty()) {
            // Lấy cuộc hẹn đầu tiên từ danh sách
            Appointment appointment = appointments.get(0);

            // Gửi thông báo khi trạng thái cuộc hẹn là "Confirmed"
            if (appointment.getAppointmentStatus() == AppointmentStatus.Confirmed) {
                // Tạo và lưu thông báo
                Notification notification = new Notification();
                notification.setUser(appointment.getUser());  // Truy cập thông tin user
                notification.setMessage("Cuộc hẹn của bạn đã được xác nhận. Vui lòng kiểm tra thông tin chi tiết.");
                notification.setNotificationDate(new Date());
                notificationRepository.save(notification);
            }
        } else {
            // Nếu không có cuộc hẹn "Pending", có thể xử lý lỗi hoặc thông báo cho người dùng
            System.out.println("Không tìm thấy cuộc hẹn đang chờ xác nhận.");
        }
    }
}