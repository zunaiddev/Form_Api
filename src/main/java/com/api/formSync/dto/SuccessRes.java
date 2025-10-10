package com.api.formSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class SuccessRes<T> {
    private HttpStatus status;
    private boolean success;
    private T data;


    public static <T> SuccessRes<T> build(T data) {
        return build(HttpStatus.OK, true, data);
    }

    public static <T> SuccessRes<T> build(HttpStatus status, T data) {
        return build(status, true, data);
    }

    public static <T> SuccessRes<T> build(HttpStatus status, boolean success, T data) {
        return new SuccessRes<>(status, success, data);
    }
}
