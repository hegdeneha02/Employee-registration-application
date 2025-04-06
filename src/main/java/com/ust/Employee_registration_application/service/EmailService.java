package com.ust.Employee_registration_application.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSetPasswordEmail(String to, String setPasswordLink, String companyId, String companyEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Set Your Flexibility Cloud Password");
        message.setText("Dear Employee,\n\n"
                + "Your registration has been approved.\n"
                + "Company ID: " + companyId + "\n"
                + "Company Email: " + companyEmail + "\n\n"
                + "Please set your password using the following link:\n"
                + setPasswordLink + "\n\n"
                + "Note: This link expires in 24 hours.\n\n"
                + "Best regards,\n"
                + "Flexibility Cloud HR Team");
        mailSender.send(message);
    }

    public void sendRejectionEmail(String email, String firstName, String lastName) {
            String subject = "Job Application Status Update";
            String message = "Dear " + firstName + " " + lastName + ",\n\n"
                    + "We regret to inform you that your application has been rejected.\n"
                    + "If you have any questions, please feel free to reach out.\n\n"
                    + "Best regards,\nCompany HR Team";

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
        }
    }



