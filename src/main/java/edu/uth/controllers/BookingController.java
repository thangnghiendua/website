package edu.uth.controllers;

import edu.uth.model.User;
import edu.uth.model.Child;
import edu.uth.model.VaccineService;
import edu.uth.model.Booking;
import edu.uth.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private DataService dataService;

    @GetMapping("/schedule/booking")
    public String bookingForm(@RequestParam(required = false) Integer childId,
                            Model model, 
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Child> children = dataService.getChildrenByUserId(user.getId());
        List<VaccineService> singleVaccines = dataService.getServicesByType("single");
        List<VaccineService> vaccinePackages = dataService.getServicesByType("package");
        
        model.addAttribute("children", children);
        model.addAttribute("singleVaccines", singleVaccines);
        model.addAttribute("vaccinePackages", vaccinePackages);
        model.addAttribute("bookingForm", new Booking());
        
        return "schedule/booking";
    }

    @PostMapping("/schedule/booking")
    public String processBooking(@RequestParam Integer childId,
                              @RequestParam Integer serviceId,
                              @RequestParam String bookingDate,
                              @RequestParam String bookingTime,
                              @RequestParam String paymentMethod,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // In a real app, you would parse bookingDate and bookingTime to create a Date object
        // And validate all input data
        
        Child child = dataService.getChildById(childId);
        VaccineService service = dataService.getServiceById(serviceId);
        
        if (child == null || service == null) {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/schedule/booking";
        }
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setChild(child);
        booking.setService(service);
        booking.setDatetime(new Date()); // In a real app, this would be the parsed date/time
        booking.setTotalAmount(service.getPrice());
        booking.setPaymentMethod(paymentMethod);
        
        dataService.addBooking(booking);
        
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/orders/list";
    }

    @GetMapping("/schedule/calendar")
    public String calendar(@RequestParam(required = false) Integer childId,
                         Model model, 
                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Child> children = dataService.getChildrenByUserId(user.getId());
        model.addAttribute("children", children);
        
        // In a real app, you would load upcoming and past vaccinations for the calendar
        
        return "schedule/calendar";
    }

    @GetMapping("/orders/list")
    public String ordersList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Booking> bookings = dataService.getBookingsByUserId(user.getId());
        model.addAttribute("bookings", bookings);
        
        return "orders/list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        Booking order = dataService.getBookingById(id);
        
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders/list";
        }
        
        model.addAttribute("order", order);
        
        return "orders/detail";
    }

    @GetMapping("/orders/{id}/payment")
    public String orderPayment(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        Booking order = dataService.getBookingById(id);
        
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders/list";
        }
        
        model.addAttribute("order", order);
        
        return "orders/payment";
    }

    @PostMapping("/orders/{id}/payment")
    public String processPayment(@PathVariable Integer id,
                               @RequestParam String paymentMethod,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        Booking order = dataService.getBookingById(id);
        
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders/list";
        }
        
        // Update payment status
        dataService.updateBookingPayment(id, true, paymentMethod);
        
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/orders/" + id;
    }
}
