package edu.uth.controllers;

import edu.uth.model.User;
import edu.uth.model.Child;
import edu.uth.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@GetMapping("/register")
public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User()); // Tạo đối tượng User mới cho form
    return "register";
}

// Xử lý form đăng ký
@PostMapping("/register")
public String processRegistration(@ModelAttribute("user") User user, 
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
    // Kiểm tra lỗi validation
    if (bindingResult.hasErrors()) {
        return "register";
    }
    
    // Kiểm tra email đã tồn tại chưa
    if (dataService.getUserByEmail(user.getEmail()) != null) {
        bindingResult.rejectValue("email", "error.user", "Email đã được sử dụng");
        return "register";
    }
    
    // Lưu người dùng mới
    // Trong demo, thêm ID mới cho user
    Integer newId = dataService.getAllUsers().stream()
            .mapToInt(User::getId)
            .max()
            .orElse(0) + 1;
    user.setId(newId);
    user.setRegistrationDate(new Date());
    
    // Thêm user vào danh sách
    dataService.getAllUsers().add(user);
    
    redirectAttributes.addAttribute("success", true);
    return "redirect:/register";

    // Children management
    @GetMapping("/children/list")
    public String listChildren(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Child> children = dataService.getChildrenByUserId(user.getId());
        model.addAttribute("children", children);
        
        return "children/list";
    }

    @GetMapping("/children/add")
    public String addChildForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("child", new Child());
        // In a real app, you would also add list of vaccines for the previous vaccinations checkbox group
        
        return "children/add";
    }

    @PostMapping("/children/add")
    public String processAddChild(@ModelAttribute Child child, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        child.setUserId(user.getId());
        dataService.addChild(child);
        
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/children/list";
    }

    @GetMapping("/children/{id}")
    public String viewChild(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        Child child = dataService.getChildById(id);
        
        if (child == null || !child.getUserId().equals(user.getId())) {
            return "redirect:/children/list";
        }
        
        model.addAttribute("child", child);
        
        return "children/detail";
    }
}