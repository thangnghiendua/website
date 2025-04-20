package ut.edu.vaccinemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.ReactionReport;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Repositories.ReactionReportRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;

import java.util.Date;
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
            return reactionReportRepository.save(report);
        }
        return null;
    }

    public List<ReactionReport> getReportsByUserId(Long userId) {
        return reactionReportRepository.findByUserUserId(userId);
    }


    public List<ReactionReport> getReactionReportsForToday(long doctorId) {
        Date todayStart = new Date();
        todayStart.setHours(0);
        todayStart.setMinutes(0);
        todayStart.setSeconds(0);

        Date todayEnd = new Date();
        todayEnd.setHours(23);
        todayEnd.setMinutes(59);
        todayEnd.setSeconds(59);

        return reactionReportRepository.findByDoctor_DoctorIdAndReportDateBetween(doctorId, todayStart, todayEnd);
    }


    public ReactionReport updateDoctorFeedback(long reactionReportId, String doctorFeedback) throws Exception {
        ReactionReport reactionReport = reactionReportRepository.findById(reactionReportId)
                .orElseThrow(() -> new Exception("Không tìm thấy báo cáo triệu chứng với ID: " + reactionReportId));

        reactionReport.setDoctorFeedback(doctorFeedback);
        return reactionReportRepository.save(reactionReport);
    }
}