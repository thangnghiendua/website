package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "Doctors")
public class Doctor {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long doctorId;

    @Column(length = 100, nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String doctorDescription;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointment;

    @OneToMany(mappedBy = "doctor")
    private List<FeedBack> feedBack;

    @OneToMany(mappedBy = "doctor")
    private List<ReactionReport> reactionReport;

    public Doctor(String doctorName, Double rating, String doctorDescription) {
        this.doctorName = doctorName;
        this.rating = rating;
        this.doctorDescription = doctorDescription;
    }

    public Doctor() {}

    public long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Double getRating() {
        return rating;
    }

    public String getDoctorDescription() {
        return doctorDescription;
    }

    public List<Appointment> getAppointment() {
        return appointment;
    }

    public List<FeedBack> getFeedBack() {
        return feedBack;
    }

    public List<ReactionReport> getReactionReport() {
        return reactionReport;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }
}
