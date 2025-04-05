package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.FeedBack;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.ReactionReport;

import java.util.List;
@Repository
public interface ReactionReportRepository extends JpaRepository<ReactionReport, Long> {
    List<ReactionReport> findByDoctor_DoctorId(long doctorId);
}
