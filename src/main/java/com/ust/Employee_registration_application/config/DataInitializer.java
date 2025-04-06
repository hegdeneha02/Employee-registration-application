package com.ust.Employee_registration_application.config;

import com.ust.Employee_registration_application.entity.Employee;
import com.ust.Employee_registration_application.repo.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.findByRolesContaining("HR").isEmpty()) {
            Employee hr = new Employee();
            hr.setFirstName("HR");
            hr.setLastName("Admin");
            hr.setPersonalEmail("hr@flexibilitycloud.com");
            hr.setCompanyEmail("hr@flexibilitycloud.com");
            hr.setEmployeeCode("FC0001");
            hr.setPassword(passwordEncoder.encode("hrpass123"));
            hr.setStatus("approved");
            // Use a mutable list instead of an immutable list
            hr.setRoles(new ArrayList<>(List.of("HR")));
            employeeRepository.save(hr);
        }
    }
}
