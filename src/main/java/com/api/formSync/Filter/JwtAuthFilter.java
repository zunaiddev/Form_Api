package com.api.formSync.Filter;

import com.api.formSync.Dto.ErrorResponse;
import com.api.formSync.Exception.InvalidHeaderException;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.Service.JwtService;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import com.api.formSync.util.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService service;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        final String URI = req.getRequestURI();
        return !URI.startsWith("/api/user") && !URI.startsWith("/api/key") && !URI.startsWith("/api/admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        try {
            String authHeader = req.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new InvalidHeaderException("Invalid or missing Authorization header");
            }

            String token = authHeader.substring(7);
            Claims claims = service.extractClaims(token);

            Purpose purpose = Purpose.from(claims.get("purpose"));

            if (purpose != Purpose.AUTHENTICATION) {
                throw new JwtException("This Token is Not Allowed for Authentication");
            }

            User user = new User();
            user.setId(Long.valueOf(claims.getSubject()));
            user.setEmail(claims.get("email", String.class));
            user.setRole(Role.valueOf(claims.get("role", String.class)));

            UserDetails details = new UserPrincipal(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(req, res);
        } catch (InvalidHeaderException e) {
            log.warn("Invalid Header. {}", e.getMessage());
            Common.setError(res, ErrorResponse.build("Authentication Failed", HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (ExpiredJwtException e) {
            log.warn("Expired Token. {}", e.getMessage());
            Common.setError(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Token has expired"));
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.warn("Invalid Token Format. {}", e.getMessage());
            Common.setError(res, ErrorResponse.build("Authentication Failed", HttpStatus.BAD_REQUEST, "Invalid token format"));
        } catch (SignatureException e) {
            log.warn("Invalid Token Signature. {}", e.getMessage());
            Common.setError(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Invalid token signature"));
        } catch (Exception e) {
            log.warn("Something went wrong. {}", e.getMessage());
            Common.setError(res, ErrorResponse.build("Authentication Failed", HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong."));
        }
    }
}