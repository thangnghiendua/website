package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.FeedBack;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
    List<FeedBack> findByDoctor_DoctorId(long doctorId);

    List<FeedBack> findByUserUserId(Long userId);
}
