package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table (name = "Payments")
public class Payment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long paymentId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "appointmentId")
    private Appointment appointment;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.Are_Trading;

    public Payment(long paymentId, User user, Appointment appointment, BigDecimal amount, Date paymentDate, PaymentStatus paymentStatus) {
        this.paymentId = paymentId;
        this.user = user;
        this.appointment = appointment;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
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

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
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
}
