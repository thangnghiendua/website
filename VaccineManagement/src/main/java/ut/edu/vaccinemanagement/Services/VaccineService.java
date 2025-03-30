package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.Repositories.VaccineRepository;
import ut.edu.vaccinemanagement.models.PackageType;
import ut.edu.vaccinemanagement.models.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccineService {
    @Autowired
    VaccineRepository vaccineRepository;

    public List<Vaccine> getAllVaccine(Vaccine vaccine) {
        return vaccineRepository.findAll();
    }

    public List<Vaccine> getAllVaccineByPackageType(PackageType packageType) {
        return vaccineRepository.findVaccineByPackageType(packageType);
    }
}
