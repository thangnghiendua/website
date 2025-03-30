package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table (name = "Reaction_Reports")
public class ReactionReport {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long reactionReportId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Doctor doctor;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String doctorFeedback;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate = new Date();

    public ReactionReport(long reactionReportId, User user, Doctor doctor, String symptoms, String doctorFeedback, Date reportDate) {
        this.reactionReportId = reactionReportId;
        this.user = user;
        this.doctor = doctor;
        this.symptoms = symptoms;
        this.doctorFeedback = doctorFeedback;
        this.reportDate = reportDate;
    }

    public ReactionReport() {}

    public long getReactionReportId() {
        return reactionReportId;
    }

    public User getUser() {
        return user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getDoctorFeedback() {
        return doctorFeedback;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReactionReportId(long reactionReportId) {
        this.reactionReportId = reactionReportId;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setDoctorFeedback(String doctorFeedback) {
        this.doctorFeedback = doctorFeedback;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}
