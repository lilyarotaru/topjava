package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    private final static String CSS_URL = "/resources/css/style.css";
    private final static String CSS_TYPE = "text/css";

    @Test
    void style() throws Exception {
        perform(MockMvcRequestBuilders.get(CSS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(CSS_TYPE));
    }
}