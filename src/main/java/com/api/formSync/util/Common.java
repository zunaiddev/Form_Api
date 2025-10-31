package com.api.formSync.util;

import com.api.formSync.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

public class Common {
    public static void setError(HttpServletResponse res, ErrorResponse error) throws IOException {
        res.setStatus((int) error.getError().get("code"));
        res.setContentType("application/json");

        PrintWriter writer = res.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        writer.write(mapper.writeValueAsString(error));
        writer.flush();
    }

    public static void setError(HttpServletResponse res, HttpStatus status, String title, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");

        PrintWriter writer = res.getWriter();

        writer.write(String.format("{\"title\": \"%s\",\"message\": \"%s\"}", title, message));
        writer.flush();
    }

    public static void setCookie(HttpServletResponse res, String token) {
        res.setHeader("Set-Cookie",
                String.format("refresh_token=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None", token, 2_592_000));
    }

    public static Cookie getCookie(String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setMaxAge(2_592_000);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }
}