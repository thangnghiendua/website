package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.User;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.UserRole;

import java.util.Optional;
import java.util.List;
@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByUserRole(UserRole userRole);
}


