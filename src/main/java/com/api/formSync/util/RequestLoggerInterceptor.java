package com.api.formSync.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Log.green("\nAuthenticated");
            System.out.print("User: ");
            Log.blue(auth.getName());
        } else {
            Log.red("\nUnauthorised");
        }

        System.out.println("Request Received: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")));
        System.out.println("Method: " + request.getMethod());
        System.out.println("URL: " + request.getRequestURI());
        System.out.println("IP: " + request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.print("Response Status: ");

        if (response.getStatus() > 300){
           Log.red(Integer.toString(response.getStatus()));
            return;
        }
        Log.green(Integer.toString(response.getStatus()));
    }
}
