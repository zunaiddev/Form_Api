package com.api.formSync.Controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    @GetMapping("/logs")
    public ResponseEntity<Resource> getLatestLog() {
        File logFile = new File("application.log");

        if (!logFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(logFile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + logFile.getName())
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
