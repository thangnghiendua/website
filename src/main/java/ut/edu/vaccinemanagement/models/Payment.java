package ut.edu.vaccinemanagement.models;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.Are_Trading;

    @Column(length = 255)
    private String description;

    public Payment(long paymentId, User user, Appointment appointment, BigDecimal amount,
                   Date paymentDate, PaymentStatus paymentStatus, String description) {
        this.paymentId = paymentId;
        this.user = user;
        this.appointment = appointment;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.description = description;
    }

    public Payment(User user, BigDecimal amount, Date paymentDate, PaymentStatus paymentStatus, String description) {
        this.user = user;
        this.appointment = null;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.description = description;
    }

    public Payment() {}

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public User getUser() {
        return user;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public String getDescription() {
        return description;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
