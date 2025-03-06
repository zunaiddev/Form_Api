package com.api.formSync.Filter;

import com.api.formSync.exception.TodayLimitReachedException;
import com.api.formSync.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final ApiKeyService keyService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/public");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String API_KEY = request.getHeader("X-API-KEY");

        if (API_KEY == null || API_KEY.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Api Key. Please Add HEADER X-API-KEY = API_KEY");
            return;
        }

        try {
            Authentication auth = keyService.getAuthentication(API_KEY);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (TodayLimitReachedException exp) {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), exp.getMessage());
        } catch (Exception exp) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), exp.getMessage());
        }

    }
}