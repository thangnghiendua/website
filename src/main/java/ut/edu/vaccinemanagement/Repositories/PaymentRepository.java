package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ut.edu.vaccinemanagement.models.Payment;
import org.springframework.stereotype.Repository;
import ut.edu.vaccinemanagement.models.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser_UserId(Long userId);

    List<Payment> findByUser_UserIdOrderByPaymentDateDesc(Long userId);

    Page<Payment> findByUser_UserId(Long userId, Pageable pageable);

    long countByUser_UserId(Long userId);

    long countByUser_UserIdAndPaymentStatus(Long userId, PaymentStatus status);

    List<Payment> findByUser_UserIdAndPaymentDateBetweenOrderByPaymentDateDesc(
            Long userId, Date startDate, Date endDate);

    List<Payment> findByUser_UserIdAndDescriptionContainingIgnoreCaseOrderByPaymentDateDesc(Long userId, String description);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.userId = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.userId = :userId AND p.paymentStatus = :status")
    BigDecimal sumAmountByUserIdAndPaymentStatus(
            @Param("userId") Long userId, @Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.userId = :userId AND p.paymentStatus = :status AND p.description = :description")
    BigDecimal sumAmountByUserIdAndPaymentStatusAndDescription(
            @Param("userId") Long userId,
            @Param("status") PaymentStatus status,
            @Param("description") String description);

    @Query("SELECT FUNCTION('MONTH', p.paymentDate) as month, COUNT(p) as count, SUM(p.amount) as amount " +
            "FROM Payment p WHERE p.user.userId = :userId AND FUNCTION('YEAR', p.paymentDate) = :year " +
            "GROUP BY FUNCTION('MONTH', p.paymentDate) ORDER BY FUNCTION('MONTH', p.paymentDate)")
    List<Object[]> getMonthlyStats(@Param("userId") Long userId, @Param("year") int year);

    @Query("SELECT p FROM Payment p WHERE p.user.userId = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPayments(@Param("userId") Long userId, Pageable pageable);

    default List<Payment> findRecentPayments(Long userId, int limit) {
        return findRecentPayments(userId, org.springframework.data.domain.PageRequest.of(0, limit));
    }


}
