package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table (name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long appointmentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "vaccine_id", nullable = false)
    private Vaccine vaccine;

    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate = new Date();

    @Column(length = 255, nullable = false)
    private String appointmentAddress;

    @Column(length = 50)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus = AppointmentStatus.Pending;

    @OneToOne(mappedBy = "appointment")
    private Notification notification;

    public Appointment(User user, Child child, Doctor doctor, Vaccine vaccine,Date appointmentDate, String appointmentAddress, String roomNumber, AppointmentStatus appointmentStatus) {
        this.user = user;
        this.child = child;
        this.doctor = doctor;
        this.vaccine = vaccine;
        this.appointmentDate = appointmentDate;
        this.appointmentAddress = appointmentAddress;
        this.roomNumber = roomNumber;
        this.appointmentStatus = appointmentStatus;
    }

    public Appointment() {}

    public Vaccine getVaccine() {
        return vaccine;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public User getUser() {
        return user;
    }

    public Child getChild() {
        return child;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentAddress() {
        return appointmentAddress;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public Notification getNotification() {return notification;}

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentAddress(String appointmentAddress) {
        this.appointmentAddress = appointmentAddress;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
