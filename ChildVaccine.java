package ut.edu.vaccinemanagement.models;

import jakarta.persistence.*;

@Entity
@Table (name = "Children_Vaccines")
public class ChildVaccine {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long childVaccineId;

    @ManyToOne
    @JoinColumn(name = "childId")
    private Child child;

    @ManyToOne
    @JoinColumn(name = "vaccineId")
    private Vaccine vaccine;

    @Column(nullable = false)
    private Integer dose;

    public ChildVaccine(long childVaccineId, Child child, Vaccine vaccine, Integer dose) {
        this.childVaccineId = childVaccineId;
        this.child = child;
        this.vaccine = vaccine;
        this.dose = dose;
    }

    public ChildVaccine() {}

    public long getChildVaccineId() {
        return childVaccineId;
    }

    public Child getChild() {
        return child;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public Integer getDose() {
        return dose;
    }

    public void setChildVaccineId(long childVaccineId) {
        this.childVaccineId = childVaccineId;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }
}
