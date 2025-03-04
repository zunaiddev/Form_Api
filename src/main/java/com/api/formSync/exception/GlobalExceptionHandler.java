package com.api.formSync.exception;

import com.api.formSync.util.Log;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, Object> errors = new HashMap<>();

        for (FieldError error : exp.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }

    @ExceptionHandler(DuplicateEntrypointEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEntrypointEmailException(DuplicateEntrypointEmailException exp) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exp.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleMessagingException(MessagingException exp) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not send an verification Email Please try resend Email");
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleInvalidTokenException(InvalidTokenException exp) {
        return new ErrorResponse(HttpStatus.GONE.value(), exp.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleTokenExpiredException(TokenExpiredException exp) {
        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), exp.getMessage());
    }

    @ExceptionHandler(CooldownNotMetException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleCooldownNotMetException(CooldownNotMetException exp) {
        return new ErrorResponse(HttpStatus.TOO_MANY_REQUESTS.value(), exp.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException exp) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exp.getMessage());
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyVerifiedException(UserAlreadyVerifiedException exp) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exp.getMessage());
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidApiKeyException(InvalidApiKeyException exp) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exp.getMessage());
    }

    @ExceptionHandler(InvalidFormIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFormIdException(InvalidFormIdException exp) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exp.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException exp) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exp.getMessage());
    }

    @ExceptionHandler(NoUserFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUsernameNotFoundException(NoUserFoundException exp) {
        Log.red("Invalid user");
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exp.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException exp) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exp.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exp) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), exp.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException exp) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exp.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exp) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exp.getMessage());
    }
}