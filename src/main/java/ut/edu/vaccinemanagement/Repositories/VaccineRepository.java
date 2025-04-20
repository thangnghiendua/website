package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.PackageType;
import ut.edu.vaccinemanagement.models.Vaccine;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    List <Vaccine> findVaccineByPackageType(PackageType packageType);

}
