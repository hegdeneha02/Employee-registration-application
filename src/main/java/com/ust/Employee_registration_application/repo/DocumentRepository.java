package com.ust.Employee_registration_application.repo;


import com.ust.Employee_registration_application.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
