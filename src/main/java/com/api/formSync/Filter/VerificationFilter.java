package com.api.formSync.Filter;

import com.api.formSync.Service.JwtService;
import com.api.formSync.Service.TokenService;
import com.api.formSync.Service.UserDetailsServiceImpl;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.exception.InvalidHeaderException;
import com.api.formSync.exception.InvalidPurposeException;
import com.api.formSync.exception.UsedTokenException;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import com.api.formSync.util.Role;
import com.api.formSync.util.VerificationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
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
            throw new InvalidHeaderException("Missing or Invalid Authorization Header");
        }

        String token = authHeader.substring(7);

        try {
            if (tokenService.isTokenUsed(authHeader)) {
                throw new UsedTokenException("Jwt Token has already been used");
            }

            Claims claims = jwtService.extractClaims(token);

            Purpose purpose = Purpose.from(claims.get("purpose"));

            Set<Purpose> verifyPurposes = Set.of(Purpose.VERIFY_USER, Purpose.RESET_PASSWORD,
                    Purpose.REACTIVATE_USER, Purpose.UPDATE_EMAIL);

            if (purpose != null && verifyPurposes.contains(purpose)) {
                throw new InvalidPurposeException("Invalid Token Purpose");
            }

            String email = claims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            VerificationToken tokenData =
                    new VerificationToken(email, claims.get("newEmail", String.class),
                            Role.valueOf(claims.get("role", String.class)),
                            Purpose.valueOf(claims.get("purpose", String.class)));

            req.setAttribute("claims", tokenData);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, AuthorityUtils.NO_AUTHORITIES);

            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(req, res);

            if (res.getStatus() >= 200 && res.getStatus() < 300) {
                //mark the token as used
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