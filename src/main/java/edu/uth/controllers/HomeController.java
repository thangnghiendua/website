package edu.uth.controllers;

import edu.uth.model.VaccineService;
import edu.uth.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private DataService dataService;

    @GetMapping("/")
    public String index(Model model) {
        // Get featured services (first 3)
        List<VaccineService> featuredServices = dataService.getAllServices().stream()
                .limit(3)
                .collect(Collectors.toList());
        
        model.addAttribute("featuredServices", featuredServices);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/services")
    public String services(Model model) {
        List<VaccineService> singleVaccines = dataService.getServicesByType("single");
        List<VaccineService> basicPackages = dataService.getAllServices().stream()
                .filter(s -> s.getType().equals("package") && s.getName().contains("cơ bản"))
                .collect(Collectors.toList());
        List<VaccineService> premiumPackages = dataService.getAllServices().stream()
                .filter(s -> s.getType().equals("package") && s.getName().contains("nâng cao"))
                .collect(Collectors.toList());
        
        model.addAttribute("singleVaccines", singleVaccines);
        model.addAttribute("basicPackages", basicPackages);
        model.addAttribute("premiumPackages", premiumPackages);
        
        return "services";
    }

    @GetMapping("/prices")
    public String prices(Model model) {
        model.addAttribute("services", dataService.getAllServices());
        return "prices";
    }

    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }
    
}
