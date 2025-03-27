package com.api.formSync.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private int statusCode;
    private HttpStatus status;
    private Object response;

    public Response(HttpStatus status, Object response) {
        this.status = status;
        this.statusCode = status.value();
        this.response = response;
    }

    public static Response build(HttpStatus status, Object response) {
        return new Response(status, response);
    }
}
