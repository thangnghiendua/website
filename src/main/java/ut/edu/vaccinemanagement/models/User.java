package ut.edu.vaccinemanagement.models;
import java.util.Date;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table (name = "Users")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(length = 100,nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(length = 100, nullable = false, unique = true)
    private String email;


    @JsonIgnore
    @Column(length = 255, nullable = false)
    private String userPassword;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserChild> userChild;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Appointment> appointment;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Payment> payment;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<FeedBack> feedBack;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ReactionReport> reactionReport;

    @JsonIgnore

    @OneToMany(mappedBy = "user")
    private List<Notification> notification;

    public User(String userName, UserRole userRole, Gender gender, Date birthDate, String email, String userPassword) {
        this.userName = userName;
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

    public String getUserName() {
        return userName;
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


    @JsonIgnore

    public String getUserPassword() {
        return userPassword;
    }


    @JsonIgnore

    public Wallet getWallet() {
        return wallet;
    }


    @JsonIgnore

    public List<UserChild> getUserChild() {
        return userChild;
    }


    @JsonIgnore

    public List<Appointment> getAppointment() {
        return appointment;
    }


    @JsonIgnore

    public List<Payment> getPayment() {
        return payment;
    }


    @JsonIgnore

    public List<FeedBack> getFeedBack() {
        return feedBack;
    }


    @JsonIgnore

    public List<ReactionReport> getReactionReport() {
        return reactionReport;
    }


    @JsonIgnore

    public List<Notification> getNotification() {
        return notification;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setFullName(String userName) {
        this.userName = userName;
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
