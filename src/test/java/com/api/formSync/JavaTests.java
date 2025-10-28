package com.api.formSync;

import com.api.formSync.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


public class JavaTests {

    @Test
    void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse err = new ErrorResponse(HttpStatus.OK.name(), "Ok ", null);

        System.out.println(mapper.writeValueAsString(err));
        String title = "Something wrong";
        String message = "Something wrong";
        System.out.println(String.format("{\"title\": \"%s\",\"message\": \"%s\"}", title, message));
    }
}
