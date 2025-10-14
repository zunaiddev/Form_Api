package com.api.formSync.Filter;

import com.api.formSync.Service.JwtService;
import com.api.formSync.Service.UserDetailsServiceImpl;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        final String URI = req.getRequestURI();
        return !URI.startsWith("/api/user") && !URI.startsWith("/api/key") && !URI.startsWith("/api/admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Missing or Invalid Authorization Header"));
            return;
        }

        String token = authHeader.substring(7);
        String username;

        try {
            username = service.extractEmail(token);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.BAD_REQUEST, "Invalid Token"));
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (service.validateToken(token, userDetails)) {

                if (service.extractClaims(token).get("purpose") == null || !service.extractClaims(token).get("purpose").equals(Purpose.AUTH.name())) {
                    log.warn("Purpose is missing or not found");
                    Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.FORBIDDEN, "Invalid token type for authentication"));
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            log.warn("Expired Token. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Token has expired"));
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.warn("Invalid Token Format. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.BAD_REQUEST, "Invalid token format"));
        } catch (SignatureException e) {
            log.warn("Invalid Token Signature. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Invalid token signature"));
        } catch (IllegalArgumentException e) {
            log.warn("Null or Empty Token. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.BAD_REQUEST, "Token cannot be null or empty"));
        } catch (Exception e) {
            log.warn("Something went wrong. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong."));
        }
    }
}