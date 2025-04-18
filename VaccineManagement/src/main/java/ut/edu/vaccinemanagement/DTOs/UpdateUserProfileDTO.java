package ut.edu.vaccinemanagement.DTOs;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserProfileDTO {
    private String userName;
    private String gender;
    private String birthDate;
}