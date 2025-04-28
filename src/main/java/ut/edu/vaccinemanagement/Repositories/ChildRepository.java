package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.Child;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.Gender;

import java.util.Date;
import java.util.Optional;
import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByChildName(String childName);
    long countByBirthDayBetween(Date startDate, Date endDate);
    long countByBirthDayBefore(Date date);
    int countByGender(Gender gender);
}
