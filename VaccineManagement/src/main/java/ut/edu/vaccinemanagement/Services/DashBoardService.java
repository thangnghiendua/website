package ut.edu.vaccinemanagement.Services;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ut.edu.vaccinemanagement.Repositories.AppointmentRepository;
import ut.edu.vaccinemanagement.Repositories.ChildRepository;
import ut.edu.vaccinemanagement.Repositories.UserRepository;
import ut.edu.vaccinemanagement.Repositories.VaccineRepository;
import ut.edu.vaccinemanagement.models.AppointmentStatus;
import ut.edu.vaccinemanagement.models.Gender;
@Service
public class DashBoardService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        summary.put("totalUsers", userRepository.count());
        summary.put("totalChildren", childRepository.count());
        summary.put("totalVaccines", vaccineRepository.count());
        summary.put("totalAppointments", appointmentRepository.count());

        Map<String, Long> appointmentsByStatus = new HashMap<>();
        for (AppointmentStatus status : AppointmentStatus.values()) {
            long count = appointmentRepository.countByAppointmentStatus(status);
            appointmentsByStatus.put(status.name(), count);
        }
        summary.put("appointmentsByStatus", appointmentsByStatus);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();

        summary.put("todayAppointments", appointmentRepository.countByAppointmentDateBetween(startOfDay, endOfDay));

        Date now = new Date();
        summary.put("upcomingAppointments", appointmentRepository.countByAppointmentDateAfterAndAppointmentStatus(
                now, AppointmentStatus.Confirmed));

        return summary;
    }

    public Map<String, Long> getChildrenByGender() {
        Map<String, Long> genderStats = new HashMap<>();

        for (Gender gender : Gender.values()) {
            long count = childRepository.countByGender(gender);
            genderStats.put(gender.name(), count);
        }

        return genderStats;
    }

    public Map<String, Long> getChildrenByAgeGroup() {
        Map<String, Long> ageGroupStats = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();

        cal.add(Calendar.YEAR, -1);
        Date twoYearsAgo = cal.getTime();

        cal.add(Calendar.YEAR, -3);
        Date fiveYearsAgo = cal.getTime();

        cal.add(Calendar.YEAR, -5);
        Date tenYearsAgo = cal.getTime();

        ageGroupStats.put("0-1", childRepository.countByBirthDayBetween(oneYearAgo, now));
        ageGroupStats.put("1-2", childRepository.countByBirthDayBetween(twoYearsAgo, oneYearAgo));
        ageGroupStats.put("2-5", childRepository.countByBirthDayBetween(fiveYearsAgo, twoYearsAgo));
        ageGroupStats.put("5-10", childRepository.countByBirthDayBetween(tenYearsAgo, fiveYearsAgo));
        ageGroupStats.put(">10", childRepository.countByBirthDayBefore(tenYearsAgo));

        return ageGroupStats;
    }

    public List<Map<String, Object>> getMonthlyAppointments(int year) {
        List<Object[]> results = appointmentRepository.findMonthlyStats(year);
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("month", result[0]);
            stat.put("count", result[1]);
            stats.add(stat);
        }

        return stats;
    }
}

