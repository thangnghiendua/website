package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table (name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long notificationId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "appointmentId", nullable = true)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus = NotificationStatus.have_not_read_yet;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.Service;

    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate = new Date();

    public Notification(String message, User user, Appointment appointment, NotificationStatus notificationStatus, NotificationType notificationType, Date notificationDate) {
        this.message = message;
        this.user = user;
        this.appointment = appointment;
        this.notificationStatus = notificationStatus;
        this.notificationType = notificationType;
        this.notificationDate = notificationDate;
    }

    public Notification() {}

    public long getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
