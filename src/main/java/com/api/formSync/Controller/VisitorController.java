package com.api.formSync.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/visitor")
@AllArgsConstructor
public class VisitorController {
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void visitorEndpoint(HttpServletRequest req, HttpServletResponse res) {
        String utcTime = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));

        String ipAddress = req.getRemoteAddr();
        String method = req.getMethod();
        String endpoint = req.getRequestURI();
        String query = req.getQueryString() != null ? "?" + req.getQueryString() : "";
        String userAgent = req.getHeader("User-Agent");

        String timeZone = req.getHeader("Time-Zone");
        String acceptLang = req.getHeader("Accept-Language");

        int status = res.getStatus();

        log.info(""" 
                        \n \n
                        🌐 VISITOR LOG:
                        ├─ Time: {}
                        ├─ Method: {}
                        ├─ Endpoint: {}{}
                        ├─ Status: {}
                        ├─ IP Address: {}
                        ├─ User-Agent: {}
                        ├─ Time-Zone (if provided): {}
                        ├─ Language: {}
                        └─ End of log
                        """,
                utcTime, method, endpoint, query, status, ipAddress, userAgent, timeZone, acceptLang);
    }
}