package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table (name = "Vaccines")
public class Vaccine {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int vaccineId;

    @Column(length = 255, nullable = false)
    private String vaccineName;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(precision = 10, scale = 4)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "vaccine")
    private List<ChildVaccine> childVaccine;

    public Vaccine(String vaccineName, PackageType packageType, BigDecimal price, String description) {
        this.vaccineName = vaccineName;
        this.packageType = packageType;
        this.price = price;
        this.description = description;
    }

    public Vaccine() {}

    public int getVaccineId() {
        return vaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public List<ChildVaccine> getChildVaccine() {
        return childVaccine;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
