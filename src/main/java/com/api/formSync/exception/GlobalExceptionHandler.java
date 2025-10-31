package com.api.formSync.exception;

import com.api.formSync.dto.ErrorRes;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.util.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //Auth Exceptions
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorRes handleEmailConflict(ConflictException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.CONFLICT, exp);
    }

    @ExceptionHandler({CouldNotFoundTokenException.class,
            UserNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleTokenNotFound(CouldNotFoundTokenException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, exp);
    }
    
    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleMissingCookie(MissingRequestCookieException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, ErrorCode.MISSING_COOKIE, "Required cookie is missing");
    }

    //Authentication Exception Handler
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException exp) {
        log.warn(exp.getMessage());
        HttpStatus status;
        ErrorCode code;
        String message;

        if (exp instanceof DisabledException) {
            status = HttpStatus.BAD_REQUEST;
            code = ErrorCode.DISABLED;
            message = "User is disabled";
        } else if (exp instanceof LockedException) {
            status = HttpStatus.LOCKED;
            code = ErrorCode.LOCKED;
            message = "User is locked";
        } else {
            status = HttpStatus.UNAUTHORIZED;
            code = ErrorCode.UNAUTHORIZED;
            message = "Unauthorized";
        }

        return new ResponseEntity<>(
                new ErrorRes(status, code, message),
                status
        );
    }

    //Jwt Exception Handler
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException exp) {
        log.warn(exp.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode code;
        String message;

        if (exp instanceof ExpiredJwtException) {
            code = ErrorCode.EXPIRED_TOKEN;
            message = "Token has expired";
        } else {
            code = ErrorCode.INVALID_TOKEN;
            message = "Invalid token";
        }

        return new ResponseEntity<>(
                new ErrorRes(status, code, message),
                status
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handle(MethodArgumentNotValidException exp) {
        log.warn("Invalid Method Argument. {}", exp.getMessage());

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : exp.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            log.warn("Validation Failed for {} Cause {}", error.getField(), error.getDefaultMessage());
        }
        return ErrorResponse.build("Invalid Argument.", HttpStatus.UNPROCESSABLE_ENTITY, errors.toString());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorRes handleNotFound(HttpRequestMethodNotSupportedException exp) {
        log.warn("A bad url get hit with bad request {}", exp.getMessage());
        return new ErrorRes(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.NONE, exp.getLocalizedMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorRes handleNoHandlerFound(NoHandlerFoundException exp) {
        log.warn("No handler found for the request {}", exp.getMessage());
        return new ErrorRes(HttpStatus.NOT_FOUND, ErrorCode.NONE, "The requested URL " + exp.getRequestURL() + " was not found on the server.");
    }
}