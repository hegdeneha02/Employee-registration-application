package com.ust.Employee_registration_application.controller;

import com.ust.Employee_registration_application.entity.Document;
import com.ust.Employee_registration_application.repo.DocumentRepository;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class DocumentController {

    private final DocumentRepository documentRepository;

    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
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

    // Optionally, if you want to view the document inline (if it's a viewable type)
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
}
