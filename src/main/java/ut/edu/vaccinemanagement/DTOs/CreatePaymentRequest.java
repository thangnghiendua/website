package ut.edu.vaccinemanagement.DTOs;
import ut.edu.vaccinemanagement.models.PaymentStatus;

import java.math.BigDecimal;

public class CreatePaymentRequest {
    public Long userId;
    public Long appointmentId;
    public BigDecimal amount;
    public String description;
    public PaymentStatus paymentStatus;

    public Long getUserId() {
        return userId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}


