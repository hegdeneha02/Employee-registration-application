package com.ust.Employee_registration_application.controller;



import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(Authentication auth) {
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR"))) {
            return "redirect:/admin/pending";
        } else {
            return "redirect:/employee/profile";
        }
    }
    @GetMapping("/layout")
    public String showHomePage() {
        return "layout"; // layout.html in templates/
    }
}
