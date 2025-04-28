package ut.edu.vaccinemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.ReactionReport;
import ut.edu.vaccinemanagement.models.User;

import ut.edu.vaccinemanagement.models.Doctor;
import ut.edu.vaccinemanagement.Repositories.ReactionReportRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import ut.edu.vaccinemanagement.Repositories.DoctorRepository;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
public class ReactionReportService {

    @Autowired
    private ReactionReportRepository reactionReportRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private DoctorRepository doctorRepository;


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

        Doctor doctor = doctorRepository.findById(report.getDoctor().getDoctorId()).orElse(null);
        
        if (user != null && doctor != null) {
            report.setUser(user);
            report.setDoctor(doctor);

            return reactionReportRepository.save(report);
        }
        return null;
    }

    public List<ReactionReport> getReportsByUserId(Long userId) {
        return reactionReportRepository.findByUserUserId(userId);
    }


    public List<ReactionReport> getTodayReports() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        return reactionReportRepository.findByReportDateBetween(
            Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())
        );
    }


    public ReactionReport updateDoctorFeedback(long reactionReportId, String doctorFeedback) throws Exception {
        ReactionReport reactionReport = reactionReportRepository.findById(reactionReportId)
                .orElseThrow(() -> new Exception("Không tìm thấy báo cáo triệu chứng với ID: " + reactionReportId));

        reactionReport.setDoctorFeedback(doctorFeedback);
        return reactionReportRepository.save(reactionReport);
    }


    public List<ReactionReport> getReactionReportsForToday(long doctorId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        return reactionReportRepository.findByDoctor_DoctorIdAndReportDateBetween(
            doctorId,
            Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())
        );
    }

    public List<ReactionReport> getAllReactionReportsForToday() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        return reactionReportRepository.findByReportDateBetween(
            Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())
        );
    }

}