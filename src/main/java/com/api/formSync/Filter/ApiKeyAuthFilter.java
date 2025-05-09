package com.api.formSync.Filter;

import com.api.formSync.Service.ApiKeyService;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.exception.ForbiddenException;
import com.api.formSync.exception.TodayLimitReachedException;
import com.api.formSync.util.Common;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;

@Slf4j
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

        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        String domain = extractDomain(origin != null ? origin : referer);

//        if (domain == null) {
//            Common.sendErrorResponse(response, ErrorResponse.build("Invalid Domain.", HttpStatus.BAD_REQUEST, "Domain is null please use direct js."));
//            return;
//        }

        try {
            Authentication auth = keyService.getAuthentication(API_KEY, domain);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (TodayLimitReachedException exp) {
            Common.sendErrorResponse(response, ErrorResponse.build("Limit Reached.", HttpStatus.TOO_MANY_REQUESTS, exp.getMessage()));
        } catch (ForbiddenException exp) {
            Common.sendErrorResponse(response, ErrorResponse.build("Forbidden", HttpStatus.FORBIDDEN, exp.getMessage()));
        } catch (Exception exp) {
            log.warn(exp.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), exp.getMessage());
        }

    }

    private String extractDomain(String url) {
        if (url == null) return null;
        try {
            URI uri = new URI(url);
            return uri.getHost();
        } catch (Exception e) {
            return null;
        }
    }
}