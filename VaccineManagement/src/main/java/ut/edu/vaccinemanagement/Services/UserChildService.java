package ut.edu.vaccinemanagement.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.Child;
import ut.edu.vaccinemanagement.models.UserChild;
import ut.edu.vaccinemanagement.Repositories.UserChildRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserChildService {

    @Autowired
    private UserChildRepository userChildRepository;

    public List<Child> getChildrenByUserId(Long userId) {
        return userChildRepository.findChildrenByUserId(userId);
    }
}
