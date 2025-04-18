package ut.edu.vaccinemanagement.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ut.edu.vaccinemanagement.models.Child;
import ut.edu.vaccinemanagement.models.UserChild;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserChildRepository extends JpaRepository<UserChild, Long> {
    @Query("SELECT uc.child FROM UserChild uc WHERE uc.user.userId = :userId")
    List<Child> findChildrenByUserId(@Param("userId") Long userId);

    // Kiểm tra xem có tồn tại liên kết giữa user và child hay không
    boolean existsByUserUserIdAndChildChildId(Long userId, Long childId);

    // Xóa liên kết giữa user và child
    void deleteByUserUserIdAndChildChildId(Long userId, Long childId);
}
