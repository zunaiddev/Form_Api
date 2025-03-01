package com.api.formSync.Filter;

import com.api.formSync.service.JwtService;
import com.api.formSync.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService service;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        final String URI = req.getRequestURI();
        return URI.startsWith("/api/auth")
                || URI.startsWith("/api/error")
                || URI.startsWith("/api/public");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Missing or Invalid Header");
            return;
        }

        String token = authHeader.substring(7);
        String username;

        try {
            username = service.extractEmail(token);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Invalid Token");
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (service.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Token has expired");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Invalid token format");
        } catch (SignatureException e) {
            sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Invalid token signature");
        } catch (IllegalArgumentException e) {
            sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Token cannot be null or empty");
        } catch (Exception e) {
            sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Invalid Token");
        }
    }

    private void sendErrorResponse(HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();
        writer.write("{ \"error\": \"" + message + "\" }");
        writer.flush();
    }
}