package ut.edu.vaccinemanagement.DTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private Long childId;
    private Long vaccineId;
}
