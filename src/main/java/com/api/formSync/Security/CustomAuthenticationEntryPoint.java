package com.api.formSync.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Custom JSON response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Check if it's a UsernameNotFoundException
        if (authException.getCause() instanceof org.springframework.security.core.userdetails.UsernameNotFoundException) {
            response.getWriter().write("{\"error\": \"User not found\"}");
        } else {
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
        }
    }
}
