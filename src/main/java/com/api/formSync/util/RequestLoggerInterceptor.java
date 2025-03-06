package com.api.formSync.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("🔹 Request Received: " + LocalDateTime.now());
        System.out.println("🔹 Method: " + request.getMethod());
        System.out.println("🔹 URL: " + request.getRequestURI());
        System.out.println("🔹 IP Address: " + request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("✅ Response Status: " + response.getStatus());
    }
}
