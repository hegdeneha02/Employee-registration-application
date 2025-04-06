package com.ust.Employee_registration_application.controller;


import com.ust.Employee_registration_application.entity.Document;
import com.ust.Employee_registration_application.entity.Employee;
import com.ust.Employee_registration_application.repo.DocumentRepository;
import com.ust.Employee_registration_application.repo.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class RegistrationController {
    private final EmployeeRepository employeeRepository;
    private final DocumentRepository documentRepository;

    public RegistrationController(EmployeeRepository employeeRepository, DocumentRepository documentRepository) {
        this.employeeRepository = employeeRepository;
        this.documentRepository = documentRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "registration";
    }

    @PostMapping("/register")
    public String registerEmployee(Employee employee,
                                   @RequestParam("aadhar") MultipartFile aadhar,
                                   @RequestParam("pan") MultipartFile pan,
                                   @RequestParam("degree") MultipartFile degree,
                                   @RequestParam("photo") MultipartFile photo) throws IOException {
        employee.setStatus("pending");
        Employee savedEmployee = employeeRepository.save(employee);

        saveDocument(savedEmployee, "AADHAR", aadhar);
        saveDocument(savedEmployee, "PAN", pan);
        saveDocument(savedEmployee, "DEGREE", degree);
        saveDocument(savedEmployee, "PHOTO", photo);

        return "redirect:/pending";
    }

    private void saveDocument(Employee employee, String type, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Document doc = new Document();
            doc.setEmployee(employee);
            doc.setDocumentType(type);
            doc.setFileContent(file.getBytes());
            documentRepository.save(doc);
        }
    }

    @GetMapping("/pending")
    public String showPending() {
        return "pending";
    }
}
