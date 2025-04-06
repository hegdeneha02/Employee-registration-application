package com.ust.Employee_registration_application.config;





import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;

public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Check if the user has HR role
        boolean isHR = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_HR"));
        // Check if the user has EMPLOYEE role
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE"));

        if (isHR) {
            response.sendRedirect("/home");
        } else if (isEmployee) {
            response.sendRedirect("/employee/profile");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}


