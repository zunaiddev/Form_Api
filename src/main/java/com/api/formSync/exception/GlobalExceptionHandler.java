package com.api.formSync.exception;

import com.api.formSync.dto.ErrorResponse;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
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
    @ExceptionHandler(DuplicateEntrypointEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse handle(DuplicateEntrypointEmailException exp) {
        log.warn("Duplicate Entry For Email Exception. Message {}", exp.getMessage());
        return ErrorResponse.build("Invalid Email.", HttpStatus.CONFLICT, exp.getMessage());
    }

    @ExceptionHandler(EmailSenderFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse handle(EmailSenderFailException exp) {
        log.warn("Email Send Fail while Signup. Message {}", exp.getMessage());
        return ErrorResponse.build("Could Not Send Email.", HttpStatus.CONFLICT, exp.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(AuthenticationException exp) {
        log.warn("Authentication Failed. Message {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.UNAUTHORIZED, "Invalid Username or Password.");
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ErrorResponse handle(DisabledException exp) {
        log.warn("Disabled User is trying To login. Message {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.FORBIDDEN, "Please Verify Your email to login.");
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private ErrorResponse handle(LockedException exp) {
        log.warn("Locked User is trying To login. Message {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.FORBIDDEN, "Your temporary disabled. please contact to admin.");
    }

    @ExceptionHandler(UnauthorisedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(UnauthorisedException exp) {
        log.error("Authentication Failed. Message {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.UNAUTHORIZED, "Bad credentials Provided.");
    }

    @ExceptionHandler(CouldNotFoundCookie.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(CouldNotFoundCookie exp) {
        log.warn("Unable To find Cookie. {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.UNAUTHORIZED, exp.getMessage());
    }

    @ExceptionHandler(UnverifiedEmailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(UnverifiedEmailException exp) {
        log.warn("User Email Not Verified and trying to login. {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.UNAUTHORIZED, exp.getMessage());
    }

    @ExceptionHandler(SomethingWentWrongException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse handle(SomethingWentWrongException exp) {
        log.error("An Unexpected Error Occurred. {}", exp.getMessage());
        return ErrorResponse.build("Something Went Wrong.", HttpStatus.INTERNAL_SERVER_ERROR, "Could Not found Cause.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ErrorResponse handle(MethodArgumentNotValidException exp) {
        log.warn("Invalid Method Argument. {}", exp.getMessage());

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : exp.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            log.warn("Validation Failed for {} Cause {}", error.getField(), error.getDefaultMessage());
        }
        return ErrorResponse.build("Invalid Argument.", HttpStatus.UNPROCESSABLE_ENTITY, errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handle(HttpMessageNotReadableException exp) {
        log.warn("Request Body was Missing. {}", exp.getMessage());
        return ErrorResponse.build("Request Body is Missing.", HttpStatus.BAD_REQUEST, "Request Body is Missing");
    }


    @ExceptionHandler(InvalidApiKeyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(InvalidTokenException exp) {
        log.error("Token Invalid After Check {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.BAD_REQUEST, exp.getMessage());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(MissingRequestCookieException exp) {
        log.error("Cookie is Missing {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.UNAUTHORIZED, "Cookie is missing.");
    }

    @ExceptionHandler(RequestBodyIsMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handle(RequestBodyIsMissingException exp) {
        log.error("Request Body is Missing {}", exp.getMessage());
        return ErrorResponse.build("Verification Failed.", HttpStatus.BAD_REQUEST, "Request Body is missing.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handle(NoHandlerFoundException exp) {
        log.error("Request To Wrong URL {}", exp.getRequestURL());
        return ErrorResponse.build("Failed", HttpStatus.BAD_REQUEST, "No Endpoint " + exp.getRequestURL() + ".");
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ErrorResponse handle(ValidationException exp) {
        log.error("Validation Failed {}", exp.getMessage());
        return ErrorResponse.build("Invalid Format.", HttpStatus.UNPROCESSABLE_ENTITY, exp.getMessage());
    }


    //Jwt Exceptions
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ErrorResponse handle(SignatureException exp) {
        log.error("Token Signature is Invalid {}", exp.getMessage());
        return ErrorResponse.build("Authentication Failed.", HttpStatus.BAD_REQUEST, "Invalid Token Signature.");
    }
}