package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

@Entity
@Table (name = "Users_Children")
public class UserChild {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long userChildId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "childId")
    private Child child;

    @Enumerated(EnumType.STRING)
    private Relationship relationship = Relationship.Guardian;

    public UserChild(long userChildId, User user, Child child, Relationship relationship) {
        this.userChildId = userChildId;
        this.user = user;
        this.child = child;
        this.relationship = relationship;
    }

    public UserChild() {}

    public long getUserChildId() {
        return userChildId;
    }

    public User getUser() {
        return user;
    }

    public Child getChild() {
        return child;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setUserChildId(long userChildId) {
        this.userChildId = userChildId;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
