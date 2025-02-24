package com.api.formSync.exception;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> validationHandler(MethodArgumentNotValidException exp) {
        Map<String, Object> errors = new HashMap<>();

        for (FieldError error : exp.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }

    @ExceptionHandler(DuplicateEntrypointEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse DuplicateEntryHandler(DuplicateEntrypointEmailException exp, HttpServletRequest req) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exp.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse MessagingExceptionHandler(MessagingException exp, HttpServletRequest req) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not send an verification Email Please try resend Email", req.getRequestURI());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidTokenHandler(InvalidTokenException exp, HttpServletRequest req) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exp.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundHandler(EntityNotFoundException exp, HttpServletRequest req) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exp.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ErrorResponse userVerifiedExceptionHandler(UserAlreadyVerifiedException exp, HttpServletRequest req) {
        return new ErrorResponse(HttpStatus.ALREADY_REPORTED.value(), exp.getMessage(), req.getRequestURI());
    }
}