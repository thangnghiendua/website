package ut.edu.vaccinemanagement.DTOs;

import ut.edu.vaccinemanagement.models.PackageType;

import java.math.BigDecimal;

public class VaccineUpdateDTO {
    private String vaccineName;
    private String description;
    private BigDecimal price;
    private PackageType packageType;

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
}
