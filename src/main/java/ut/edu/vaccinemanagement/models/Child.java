package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "Children")
public class Child {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long childId;

    @Column(length = 100, nullable = false)
    private String childName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date birthDay;


    @JsonIgnore
    @OneToMany(mappedBy = "child")
    private List<UserChild> userChild;

    @JsonIgnore
    @OneToMany(mappedBy = "child")
    private List<Appointment> appointment;

    @JsonIgnore

    @OneToMany(mappedBy = "child")
    private List<ChildVaccine> childVaccine;

    public Child(String childName, Gender gender, Date birthDay) {
        this.childName = childName;
        this.gender = gender;
        this.birthDay = birthDay;
    }

    public Child() {}

    public long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public List<UserChild> getUserChild() {
        return userChild;
    }

    public List<Appointment> getAppointment() {
        return appointment;
    }

    public List<ChildVaccine> getChildVaccine() {
        return childVaccine;
    }

    public void setChildId(long childId) {
        this.childId = childId;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}

