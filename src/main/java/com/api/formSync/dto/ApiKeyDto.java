package com.api.formSync.dto;

import com.api.formSync.model.ApiKey;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiKeyDto {
    private Long id;
    private String key;
    private int requests = 10;
    private int requestsLeft;
    private LocalDate lastUsed;

    public ApiKeyDto(ApiKey apiKey) {
        this.id = apiKey.getId();
        this.key = apiKey.getApiKey();
        this.requestsLeft = requests - apiKey.getRequestCount();
        this.lastUsed = apiKey.getLastReset();
    }
}
