package com.api.formSync.Filter;

import com.api.formSync.Service.JwtService;
import com.api.formSync.Service.TokenService;
import com.api.formSync.Service.UserDetailsServiceImpl;
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
        String token = req.getParameter("token");

        if (token == null) {
            Common.sendErrorResponse(res, HttpStatus.BAD_REQUEST, "token is missing");
        }

        try {
            String email = jwtService.extractEmail(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                if (tokenService.isTokenUsed(token)) {
                    Common.sendErrorResponse(res, HttpStatus.IM_USED, "Url Already used.");
                    return;
                }

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
            Common.sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Token has expired");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            Common.sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Invalid token format");
        } catch (SignatureException e) {
            Common.sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Invalid token signature");
        } catch (IllegalArgumentException e) {
            Common.sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Token cannot be null or empty");
        } catch (Exception e) {
            Common.sendErrorResponse(res, HttpStatus.BAD_REQUEST, "Invalid Token");
        }
    }
}