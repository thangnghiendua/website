package edu.uth.service;

import edu.uth.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class DataService {
    private List<User> users = new ArrayList<>();
    private List<Child> children = new ArrayList<>();
    private List<VaccineService> services = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DataService() {
        initializeData();
    }

    private void initializeData() {
        // Create sample users
        User user1 = new User(1, "Nguyễn Văn A", "nguyenvana@example.com", "0123456789");
        user1.setAvatarUrl("/images/avatar1.jpg");
        
        User user2 = new User(2, "Trần Thị B", "tranthib@example.com", "0987654321");
        user2.setAvatarUrl("/images/avatar2.jpg");
        
        users.add(user1);
        users.add(user2);

        // Create sample children
        Child child1 = null;
        Child child2 = null;
        Child child3 = null;
        
        try {
            child1 = new Child(1, "Nguyễn Văn An", dateFormat.parse("01/01/2020"), "M");
            child1.setUserId(1);
            child1.setAvatarUrl("/images/child-placeholder.png");
            child1.setCompletedVaccinations(5);
            child1.setWeight(12.5);
            child1.setHeight(90.0);
            
            child2 = new Child(2, "Nguyễn Thị Anh", dateFormat.parse("15/06/2022"), "F");
            child2.setUserId(1);
            child2.setAvatarUrl("/images/child-placeholder.png");
            child2.setCompletedVaccinations(2);
            child2.setWeight(8.0);
            child2.setHeight(70.0);
            
            child3 = new Child(3, "Trần Bình", dateFormat.parse("10/10/2021"), "M");
            child3.setUserId(2);
            child3.setAvatarUrl("/images/child-placeholder.png");
            child3.setCompletedVaccinations(3);
            child3.setWeight(10.0);
            child3.setHeight(80.0);
            
            children.add(child1);
            children.add(child2);
            children.add(child3);
            
            user1.addChild(child1);
            user1.addChild(child2);
            user2.addChild(child3);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create sample services
        VaccineService service1 = new VaccineService(1, "Vaccine Viêm gan B", "Phòng ngừa bệnh viêm gan B", 300000.0, "single");
        service1.setImageUrl("/images/vaccine1.jpg");
        service1.setDescription("Vaccine Viêm gan B giúp phòng ngừa bệnh viêm gan B, một bệnh viêm gan virus có thể gây tổn thương gan nghiêm trọng và dẫn đến ung thư gan.");
        
        VaccineService service2 = new VaccineService(2, "Vaccine Bạch hầu - Ho gà - Uốn ván (DPT)", "Phòng ngừa bệnh bạch hầu, ho gà, uốn ván", 350000.0, "single");
        service2.setImageUrl("/images/vaccine2.jpg");
        service2.setDescription("Vaccine DPT giúp phòng ngừa 3 bệnh nguy hiểm: Bạch hầu (Diphtheria), Ho gà (Pertussis) và Uốn ván (Tetanus).");
        
        VaccineService package1 = new VaccineService(101, "Gói tiêm chủng cơ bản 0-12 tháng", "Gói vaccine phòng ngừa các bệnh phổ biến cho trẻ từ 0-12 tháng tuổi", 1200000.0, "package");
        package1.setImageUrl("/images/package1.jpg");
        package1.setDescription("Gói tiêm chủng cơ bản cho trẻ từ 0-12 tháng tuổi, bao gồm các vaccine phòng ngừa bệnh phổ biến và nguy hiểm.");
        package1.setItems(Arrays.asList("Vaccine Viêm gan B", "Vaccine BCG", "Vaccine DPT", "Vaccine Bại liệt", "Vaccine Hib"));
        
        VaccineService package2 = new VaccineService(102, "Gói tiêm chủng nâng cao 0-12 tháng", "Gói vaccine nâng cao phòng ngừa nhiều bệnh hơn cho trẻ từ 0-12 tháng tuổi", 2500000.0, "package");
        package2.setImageUrl("/images/package2.jpg");
        package2.setDescription("Gói tiêm chủng nâng cao cho trẻ từ 0-12 tháng tuổi, bao gồm tất cả các vaccine trong gói cơ bản và thêm nhiều vaccine quan trọng khác.");
        package2.setItems(Arrays.asList("Tất cả vaccine trong gói cơ bản", "Vaccine Viêm não Nhật Bản", "Vaccine Thủy đậu", "Vaccine Viêm phổi", "Vaccine Rotavirus"));
        
        services.add(service1);
        services.add(service2);
        services.add(package1);
        services.add(package2);

        // Create sample bookings
        if (child1 != null && child2 != null && child3 != null) {
            try {
                // Format for date with time
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                // Booking 1
                Booking booking1 = new Booking();
                booking1.setId(1);
                booking1.setUser(user1);
                booking1.setChild(child1);
                booking1.setService(service1);
                booking1.setDatetime(dateTimeFormat.parse("30/06/2023 09:00"));
                booking1.setStatus("confirmed");
                booking1.setIsPaid(false);
                booking1.setTotalAmount(service1.getPrice());
                booking1.setCreatedAt(dateTimeFormat.parse("28/06/2023 09:15"));
                booking1.setConfirmedAt(dateTimeFormat.parse("28/06/2023 10:30"));
                
                // Booking 2
                Booking booking2 = new Booking();
                booking2.setId(2);
                booking2.setUser(user2);
                booking2.setChild(child3);
                booking2.setService(package1);
                booking2.setDatetime(dateTimeFormat.parse("01/07/2023 14:30"));
                booking2.setStatus("confirmed");
                booking2.setIsPaid(true);
                booking2.setPaymentMethod("banking");
                booking2.setTotalAmount(package1.getPrice());
                booking2.setCreatedAt(dateTimeFormat.parse("27/06/2023 15:45"));
                booking2.setConfirmedAt(dateTimeFormat.parse("27/06/2023 16:30"));
                
                // Booking 3 - completed
                Booking booking3 = new Booking();
                booking3.setId(3);
                booking3.setUser(user1);
                booking3.setChild(child2);
                booking3.setService(service2);
                booking3.setDatetime(dateTimeFormat.parse("25/06/2023 10:00"));
                booking3.setStatus("completed");
                booking3.setIsPaid(true);
                booking3.setPaymentMethod("cash");
                booking3.setTotalAmount(service2.getPrice());
                booking3.setCreatedAt(dateTimeFormat.parse("20/06/2023 11:30"));
                booking3.setConfirmedAt(dateTimeFormat.parse("20/06/2023 12:15"));
                booking3.setCompletedAt(dateTimeFormat.parse("25/06/2023 10:45"));
                booking3.setNotes("Tiêm chủng thành công, trẻ không có phản ứng sau tiêm.");
                
                bookings.add(booking1);
                bookings.add(booking2);
                bookings.add(booking3);
                
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // User methods
    public List<User> getAllUsers() {
        return users;
    }
    
    public User getUserById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Child methods
    public List<Child> getAllChildren() {
        return children;
    }
    
    public List<Child> getChildrenByUserId(Integer userId) {
        return children.stream()
                .filter(child -> child.getUserId().equals(userId))
                .toList();
    }
    
    public Child getChildById(Integer id) {
        return children.stream()
                .filter(child -> child.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public void addChild(Child child) {
        // Generate a new ID
        Integer newId = children.stream()
                .mapToInt(Child::getId)
                .max()
                .orElse(0) + 1;
        
        child.setId(newId);
        children.add(child);
        
        // Update user's children list
        User user = getUserById(child.getUserId());
        if (user != null) {
            user.addChild(child);
        }
    }

    // Service methods
    public List<VaccineService> getAllServices() {
        return services;
    }
    
    public List<VaccineService> getServicesByType(String type) {
        return services.stream()
                .filter(service -> service.getType().equals(type))
                .toList();
    }
    
    public VaccineService getServiceById(Integer id) {
        return services.stream()
                .filter(service -> service.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Booking methods
    public List<Booking> getAllBookings() {
        return bookings;
    }
    
    public List<Booking> getBookingsByUserId(Integer userId) {
        return bookings.stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .toList();
    }
    
    public Booking getBookingById(Integer id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public void addBooking(Booking booking) {
        // Generate a new ID
        Integer newId = bookings.stream()
                .mapToInt(Booking::getId)
                .max()
                .orElse(0) + 1;
        
        booking.setId(newId);
        booking.setCreatedAt(new Date());
        booking.setStatus("pending");
        booking.setIsPaid(false);
        
        bookings.add(booking);
    }
    
    public void updateBookingStatus(Integer id, String status) {
        Booking booking = getBookingById(id);
        if (booking != null) {
            booking.setStatus(status);
            
            Date now = new Date();
            if ("confirmed".equals(status)) {
                booking.setConfirmedAt(now);
            } else if ("completed".equals(status)) {
                booking.setCompletedAt(now);
            } else if ("cancelled".equals(status)) {
                booking.setCancelledAt(now);
            }
        }
    }
    
    public void updateBookingPayment(Integer id, Boolean isPaid, String paymentMethod) {
        Booking booking = getBookingById(id);
        if (booking != null) {
            booking.setIsPaid(isPaid);
            booking.setPaymentMethod(paymentMethod);
        }
    }
}
