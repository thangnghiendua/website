package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.DTOs.VaccineUpdateDTO;
import ut.edu.vaccinemanagement.Repositories.VaccineRepository;
import ut.edu.vaccinemanagement.models.PackageType;
import ut.edu.vaccinemanagement.models.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VaccineService {
    @Autowired
    VaccineRepository vaccineRepository;

    public List<Vaccine> getAllVaccine() {
        return vaccineRepository.findAll();
    }

    public List<Vaccine> getAllVaccineByPackageType(PackageType packageType) {
        return vaccineRepository.findVaccineByPackageType(packageType);
    }

    public Vaccine updateVaccine(Long id, VaccineUpdateDTO dto) {
        Optional<Vaccine> optionalVaccine = vaccineRepository.findById(id);
        if (optionalVaccine.isPresent()) {
            Vaccine vaccine = optionalVaccine.get();

            if (dto.getVaccineName() != null && !dto.getVaccineName().isBlank()) {
                vaccine.setVaccineName(dto.getVaccineName());
            }

            if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
                vaccine.setDescription(dto.getDescription());
            }

            if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                vaccine.setPrice(dto.getPrice());
            }

            if (dto.getPackageType() != null) {
                vaccine.setPackageType(dto.getPackageType());
            }

            return vaccineRepository.save(vaccine);
        } else {
            throw new RuntimeException("Không tìm thấy Vaccine với ID = " + id);
        }
    }
}
