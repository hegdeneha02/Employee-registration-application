package com.ust.Employee_registration_application.controller;


import com.ust.Employee_registration_application.entity.Employee;
import com.ust.Employee_registration_application.repo.EmployeeRepository;
import com.ust.Employee_registration_application.service.EmailService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Secured("ROLE_HR")
public class AdminController {
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public AdminController(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @GetMapping("/pending")
    public String listEmployeeRegistrations(Model model) {
        model.addAttribute("pendingEmployees", employeeRepository.findByStatus("pending"));
        model.addAttribute("approvedEmployees", employeeRepository.findByStatus("approved"));
        model.addAttribute("rejectedEmployees", employeeRepository.findByStatus("rejected"));
        return "admin/pending";  // This will map to your Thymeleaf file: admin/registrations.html
    }

    @GetMapping("/employee/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        if ("pending".equalsIgnoreCase(employee.getStatus())) {
            model.addAttribute("employee", employee);
            return "admin/employee-details"; // Redirects to another page for pending status
        }

        model.addAttribute("employee", employee);
        return "admin/employee-view"; // Normal details page
    }

    @PostMapping("/approve/{id}")
    public String approveEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
        employee.setEmployeeCode(generateEmployeeCode());
        employee.setCompanyEmail(generateCompanyEmail(employee.getFirstName(), employee.getLastName()));
        employee.setRoles(new ArrayList<>(List.of("EMPLOYEE")));
        employee.setStatus("approved");

        String token = UUID.randomUUID().toString();
        employee.setPasswordResetToken(token);
        employee.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));

        employeeRepository.save(employee);

        String setPasswordLink = "http://localhost:8082/set-password?token=" + token;
        emailService.sendSetPasswordEmail(
                employee.getPersonalEmail(),
                setPasswordLink,
                employee.getEmployeeCode(),
                employee.getCompanyEmail()
        );
        return "redirect:/admin/pending";
    }

    @PostMapping("/reject/{id}")
    public String rejectEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        employee.setStatus("rejected");
        employeeRepository.save(employee);

        // Send rejection email
        emailService.sendRejectionEmail(employee.getPersonalEmail(), employee.getFirstName(), employee.getLastName());

        return "redirect:/admin/pending";
    }



    private String generateEmployeeCode() {
        return "FC" + LocalDate.now().getYear() + String.format("%04d", employeeRepository.count() + 1);
    }

    private String generateCompanyEmail(String firstName, String lastName) {
        String base = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@flexibilitycloud.com";
        int suffix = 1;
        while (employeeRepository.findByCompanyEmail(base).isPresent()) {
            base = firstName.toLowerCase() + "." + lastName.toLowerCase() + suffix++ + "@flexibilitycloud.com";
        }
        return base;
    }
}
