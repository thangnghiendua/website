package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.Doctor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
