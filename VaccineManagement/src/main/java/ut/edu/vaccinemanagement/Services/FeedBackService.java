package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.Security.CustomUserDetails;
import ut.edu.vaccinemanagement.models.FeedBack;
import ut.edu.vaccinemanagement.models.Doctor;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.Repositories.FeedBackRepository;
import ut.edu.vaccinemanagement.Repositories.DoctorRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    public FeedBack addFeedBack(Long doctorId, String comment, Integer rating) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId;

        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new RuntimeException("Không thể xác định user!");
        }

        Optional<User> user = userRepository.findById(userId);
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (user.isEmpty() || doctor.isEmpty()) {
            throw new RuntimeException("User hoặc Doctor không tồn tại!");
        }

        FeedBack feedback = new FeedBack();
        feedback.setFeedbackDate(new Date());
        feedback.setUser(user.get());
        feedback.setDoctor(doctor.get());
        feedback.setComment(comment);
        feedback.setRating(rating);

        return feedBackRepository.save(feedback);
    }
}