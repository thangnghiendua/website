package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.Repositories.ChildRepository;
import ut.edu.vaccinemanagement.models.Child;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.Gender;

import java.util.Date;
import java.util.List;

@Service
public class ChildService {
    @Autowired
    ChildRepository childRepository;

    public List<Child> findByChildName(String childName) {
        return childRepository.findByChildName(childName);
    }

    public Child addChild(Child child) {
        if(child.getChildName() == null || child.getBirthDay() == null || child.getGender() == null ) {
            throw new IllegalArgumentException("Thông tin trẻ không được để trống!");
        }
        return childRepository.save(child);
    }

    public Child updateChildPartial(Long id, String childName, String gender, String birthDay) {
        return childRepository.findById(id).map(child -> {
            if (childName != null && !childName.isEmpty()) {
                child.setChildName(childName);
            }
            if (gender != null) {
                try {
                    String normalizedGender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                    child.setGender(Gender.valueOf(normalizedGender));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Giới tính không hợp lệ: " + gender);
                }
            }
            if (birthDay != null) {
                try {
                    Date date = java.sql.Date.valueOf(birthDay);
                    child.setBirthDay(date);
                } catch (Exception e) {
                    throw new RuntimeException("Ngày sinh không hợp lệ");
                }
            }
            return childRepository.save(child);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy trẻ em với ID: " + id));
    }

    public void deleteChild(Long id) {
        if (childRepository.existsById(id)) {
            childRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy trẻ có id là: " + id);
        }
    }
}
