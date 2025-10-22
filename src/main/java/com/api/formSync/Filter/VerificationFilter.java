package com.api.formSync.Filter;

import com.api.formSync.Service.JwtService;
import com.api.formSync.Service.TokenService;
import com.api.formSync.Service.UserDetailsServiceImpl;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.util.Common;
import io.jsonwebtoken.Claims;
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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class VerificationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return !req.getRequestURI().startsWith("/api/verify");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing Or In valid Header, {}", authHeader);
            Common.sendErrorResponse(res, ErrorResponse.build("Authentication Failed", HttpStatus.UNAUTHORIZED, "Missing or Invalid Authorization Header"));
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractSubject(token);

            if (tokenService.isTokenUsed(authHeader)) {
                Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.IM_USED, "Url Already used."));
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                Claims claims = jwtService.extractClaims(token);
                req.setAttribute("email", email);
                req.setAttribute("purpose", claims.get("purpose"));
                req.setAttribute("newEmail", claims.get("newEmail"));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, AuthorityUtils.NO_AUTHORITIES
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                chain.doFilter(req, res);
            }
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired. Message {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.UNAUTHORIZED, "Token has expired"));
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid token Format. Message {}", "Someone change the token");
            Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.BAD_REQUEST, "Invalid token format"));
        } catch (SignatureException e) {
            log.error("Invalid Token Signature. Message {}", "Invalid Jwt token Signature.");
            Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.UNAUTHORIZED, "Invalid token signature"));
        } catch (IllegalArgumentException e) {
            log.error("Token is null or Empty.");
            Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.BAD_REQUEST, "Token cannot be null or empty"));
        } catch (Exception e) {
            log.error("Invalid Token. {}", e.getMessage());
            Common.sendErrorResponse(res, ErrorResponse.build("Verification Failed", HttpStatus.BAD_REQUEST, "Invalid Token"));
        }
    }
}