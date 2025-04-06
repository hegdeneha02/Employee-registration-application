package com.ust.Employee_registration_application.controller;


import com.ust.Employee_registration_application.entity.Employee;
import com.ust.Employee_registration_application.repo.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class AuthController {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/set-password")
    public String showSetPassword(@RequestParam("token") String token, Model model) {
        Employee employee = employeeRepository.findByPasswordResetToken(token);
        if (employee == null || employee.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token");
            return "set-password";
        }
        model.addAttribute("token", token);
        return "set-password";
    }

    @PostMapping("/set-password")
    public String setPassword(@RequestParam("token") String token,
                              @RequestParam("password") String password,
                              @RequestParam("confirmPassword") String confirmPassword,
                              Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "set-password";
        }
        Employee employee = employeeRepository.findByPasswordResetToken(token);
        if (employee == null || employee.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token");
            return "set-password";
        }
        employee.setPassword(passwordEncoder.encode(password));
        employee.setPasswordResetToken(null);
        employee.setPasswordResetTokenExpiry(null);
        employeeRepository.save(employee);
        return "redirect:/login";
    }
}