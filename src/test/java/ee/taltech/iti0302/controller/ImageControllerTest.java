package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.AbstractIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest extends AbstractIntegration {

    @Autowired
    private MockMvc mvc;

    @Test
    void getImageById() throws Exception {
        mvc.perform(get("/api/public/images/1").with(user("user")))
                .andExpect(status().isOk());
    }
}