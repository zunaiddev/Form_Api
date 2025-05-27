package com.api.formSync.util;

import com.api.formSync.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class Common {
    public static void sendErrorResponse(HttpServletResponse res, ErrorResponse error) throws IOException {
        res.setStatus((int) error.getError().get("code"));
        res.setContentType("application/json");

        PrintWriter writer = res.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        writer.write(mapper.writeValueAsString(error));
        writer.flush();
    }

    public static void setCookie(HttpServletResponse res, String token) {
        res.setHeader("Set-Cookie",
                String.format("refresh_token=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None", token, 2_592_000));
    }
}