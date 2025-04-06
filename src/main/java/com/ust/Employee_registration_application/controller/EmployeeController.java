package com.ust.Employee_registration_application.controller;


import com.ust.Employee_registration_application.entity.Document;
import com.ust.Employee_registration_application.entity.Employee;
import com.ust.Employee_registration_application.repo.DocumentRepository;
import com.ust.Employee_registration_application.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee")
@Secured("ROLE_EMPLOYEE")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication auth) {
        Employee employee = employeeRepository.findByCompanyEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        model.addAttribute("employee", employee);
        return "employee/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Employee updatedEmployee, Authentication auth) {
        Employee employee = employeeRepository.findByCompanyEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setPersonalEmail(updatedEmployee.getPersonalEmail());
        employeeRepository.save(employee);
        return "redirect:/employee/profile";
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID"));

        HttpHeaders headers = new HttpHeaders();
        // For inline viewing, you might need to determine the correct content type (e.g., image/jpeg, application/pdf, etc.)
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.inline().filename(doc.getDocumentType() + "_" + id + ".bin").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(doc.getFileContent());
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID"));

        HttpHeaders headers = new HttpHeaders();
        // Set a generic content type. Adjust if you know the specific file type.
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Build Content-Disposition header for download
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(doc.getDocumentType() + "_" + id + ".bin")
                .build();
        headers.setContentDisposition(disposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(doc.getFileContent());
    }


}
