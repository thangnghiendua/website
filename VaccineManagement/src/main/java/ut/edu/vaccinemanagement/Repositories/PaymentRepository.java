package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.Payment;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);


}
