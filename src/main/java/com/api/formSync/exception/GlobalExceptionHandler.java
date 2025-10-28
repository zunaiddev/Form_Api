package com.api.formSync.exception;

import com.api.formSync.dto.ErrorRes;
import com.api.formSync.dto.ErrorResponse;
import com.api.formSync.util.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //Auth Exceptions
    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorRes handleEmailConflict(DuplicateEmailException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.CONFLICT, exp);
    }

    @ExceptionHandler({BadCredentialsException.class, UnauthorisedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorRes handleInvalidUsername(AuthenticationException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleDisabledUser(DisabledException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, ErrorCode.DISABLED, exp.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ErrorRes handleLockedUser(LockedException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.LOCKED, ErrorCode.LOCKED, exp.getMessage());
    }

    @ExceptionHandler(CouldNotFoundTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleTokenNotFound(CouldNotFoundTokenException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, exp);
    }

    // JWT Exceptions
    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleExpiredToken(ExpiredJwtException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, ErrorCode.EXPIRED_TOKEN, "Token has expired");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleJwtException(JwtException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_TOKEN, exp.getMessage());
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorRes handleNotFound(HttpRequestMethodNotSupportedException exp) {
        log.warn("A bad url get hit with bad request {}", exp.getMessage());
        return new ErrorRes(HttpStatus.NOT_FOUND, ErrorCode.NONE, exp.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRes handleInvalidHeader(InvalidHeaderException exp) {
        log.warn(exp.getMessage());
        return new ErrorRes(HttpStatus.BAD_REQUEST, ErrorCode.NONE, exp.getLocalizedMessage());
    }
}