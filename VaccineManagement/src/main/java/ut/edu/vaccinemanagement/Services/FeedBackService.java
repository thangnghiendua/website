package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.Repositories.DoctorRepository;
import ut.edu.vaccinemanagement.models.FeedBack;
import ut.edu.vaccinemanagement.Repositories.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository feedbackRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<FeedBack> getFeedbacksByDoctorId(long doctorId) {
        return feedbackRepository.findByDoctor_DoctorId(doctorId);
    }
    public FeedBack sendFeedback(FeedBack feedback) {
        if (!doctorRepository.existsById(feedback.getDoctor().getDoctorId())) {
            throw new IllegalArgumentException("Bác sĩ không tồn tại");
        }

        return feedbackRepository.save(feedback);
    }

    public List<FeedBack> getFeedbacksByUserId(Long userId) {
        return feedbackRepository.findByUserUserId(userId);
    }
}