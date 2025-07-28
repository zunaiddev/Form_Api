package com.api.formSync;

import com.api.formSync.model.Form;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class FormSyncApplicationTests {

    @Test
    void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"Form\",\"subject\":\"last added forms4 \",\"email\":\"ja7667924@gmail.com\",\"message\":\"sent by zunaidndvsdf sdvgsdsd ghsdcty sddscf\"}";


        Form form = mapper.readValue(json, Form.class);

        System.out.println(form);
    }
}
