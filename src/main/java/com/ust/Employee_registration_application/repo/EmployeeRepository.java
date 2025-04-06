package com.ust.Employee_registration_application.repo;



import com.ust.Employee_registration_application.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCompanyEmail(String companyEmail);
    Employee findByPasswordResetToken(String token);
    List<Employee> findByStatus(String status);
    List<Employee> findByRolesContaining(String role);
}
