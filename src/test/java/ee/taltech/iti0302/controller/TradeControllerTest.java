package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.AbstractIntegration;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TradeControllerTest extends AbstractIntegration {

    @Autowired
    private MockMvc mvc;

    @Test
    void getTrades() throws Exception {
        mvc.perform(get("/api/trades").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].buyerId").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(2));
    }

    @Test
    void addTrade() throws Exception {

        JSONObject tradeJson = new JSONObject();
        tradeJson.put("id", 1);
        tradeJson.put("buyerId", 1);
        tradeJson.put("sellerId", 2);

        int productId = 1;
        mvc.perform(post("/api/trades/" + productId).with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(tradeJson.toString()))
                .andExpect(status().isOk());

        mvc.perform(get("/api/trades").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].buyerId").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(2));
    }
}