package ut.edu.vaccinemanagement.models;
import java.util.Date;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table (name = "Users")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(length = 100,nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String userPassword;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(mappedBy = "user")
    private List<UserChild> userChild;

    @OneToMany(mappedBy = "user")
    private List<Appointment> appointment;

    @OneToMany(mappedBy = "user")
    private List<Payment> payment;

    @OneToMany(mappedBy = "user")
    private List<FeedBack> feedBack;

    @OneToMany(mappedBy = "user")
    private List<ReactionReport> reactionReport;

    @OneToMany(mappedBy = "user")
    private List<Notification> notification;

    public User(String fullName, UserRole userRole, Gender gender, Date birthDate, String email, String userPassword) {
        this.fullName = fullName;
        this.userRole = userRole;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.userPassword = userPassword;
    }

    public User() {}

    public long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<UserChild> getUserChild() {
        return userChild;
    }

    public List<Appointment> getAppointment() {
        return appointment;
    }

    public List<Payment> getPayment() {
        return payment;
    }

    public List<FeedBack> getFeedBack() {
        return feedBack;
    }

    public List<ReactionReport> getReactionReport() {
        return reactionReport;
    }

    public List<Notification> getNotification() {
        return notification;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
