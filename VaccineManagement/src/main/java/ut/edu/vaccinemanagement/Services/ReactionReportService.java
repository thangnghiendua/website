package ut.edu.vaccinemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.ReactionReport;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Repositories.ReactionReportRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;

import java.util.List;

@Service
public class ReactionReportService {

    @Autowired
    private ReactionReportRepository reactionReportRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReactionReport> getReportsByDoctor(long doctorId) {
        return reactionReportRepository.findByDoctor_DoctorId(doctorId);
    }

    public ReactionReport addDoctorFeedback(long reportId, String doctorFeedback) {
        ReactionReport report = reactionReportRepository.findById(reportId).orElse(null);
        if (report != null) {
            report.setDoctorFeedback(doctorFeedback);
            return reactionReportRepository.save(report);
        }
        return null;
    }

    public ReactionReport reportSymptoms(long userId, ReactionReport report) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            report.setUser(user);
            return reactionReportRepository.save(report); // Lưu báo cáo triệu chứng của người dùng
        }
        return null;
    }
}