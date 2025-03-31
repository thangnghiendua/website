package edu.uth.controllers;

import edu.uth.model.User;
import edu.uth.model.Booking;
import edu.uth.model.VaccineService;
import edu.uth.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DataService dataService;

    // Simulate admin login for testing
    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String processAdminLogin(HttpSession session) {
        // Simulate admin login - in a real app, you would validate credentials
        User admin = new User(999, "Admin User", "admin@example.com", "0123456789");
        admin.setAvatarUrl("/images/admin-avatar.jpg");
        session.setAttribute("admin", admin);
        
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("bookings", dataService.getAllBookings());
        model.addAttribute("users", dataService.getAllUsers());
        
        return "admin/dashboard";
    }

    @GetMapping("/bookings")
    public String bookings(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("bookings", dataService.getAllBookings());
        
        return "admin/bookings";
    }

    @GetMapping("/bookings/{id}")
    public String bookingDetail(@PathVariable Integer id, Model model, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        Booking booking = dataService.getBookingById(id);
        
        if (booking == null) {
            return "redirect:/admin/bookings";
        }
        
        model.addAttribute("booking", booking);
        
        return "admin/booking-detail";
    }

    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Integer id,
                                   @RequestParam String status,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        dataService.updateBookingStatus(id, status);
        
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/admin/bookings/" + id;
    }

    @GetMapping("/customers")
    public String customers(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("users", dataService.getAllUsers());
        
        return "admin/customers";
    }

    @GetMapping("/services")
    public String services(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("services", dataService.getAllServices());
        
        return "admin/services";
    }
}