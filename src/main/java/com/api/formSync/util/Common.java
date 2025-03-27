package com.api.formSync.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

public class Common {
    public static void sendErrorResponse(HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();
        writer.write("{ \"error\": \"" + message + "\" }");
        writer.flush();
    }
}
