package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ut.edu.vaccinemanagement.models.Wallet;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional <Wallet> findByUser_UserId(Long userId);

}
