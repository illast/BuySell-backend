package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.AbstractIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemeControllerTest extends AbstractIntegration {

    @Autowired
    private MockMvc mvc;

    @Test
    void getMeme() throws Exception {
        mvc.perform(get("/api/public/meme").with(user("user")))
                .andExpect(status().isOk());
    }
}