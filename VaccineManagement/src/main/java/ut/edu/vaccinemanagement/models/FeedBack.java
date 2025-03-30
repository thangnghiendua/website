package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table (name = "FeedBacks")
public class FeedBack {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long feedBackId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Doctor doctor;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date feedbackDate = new Date();

    public FeedBack(long feedBackId, User user, Doctor doctor, String comment, Integer rating, Date feedbackDate) {
        this.feedBackId = feedBackId;
        this.user = user;
        this.doctor = doctor;
        this.comment = comment;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
    }

    public FeedBack() {}

    public long getFeedBackId() {
        return feedBackId;
    }

    public User getUser() {
        return user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedBackId(long feedBackId) {
        this.feedBackId = feedBackId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
