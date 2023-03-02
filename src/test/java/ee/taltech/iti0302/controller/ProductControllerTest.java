package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.AbstractIntegration;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest extends AbstractIntegration {

    @Autowired
    private MockMvc mvc;

    @Test
    void getProducts() throws Exception {
        mvc.perform(get("/api/public/products").with(user("user")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name").value("Milk"))
            .andExpect(jsonPath("$[1].name").value("Eggs"))
            .andExpect(jsonPath("$[2].name").value("Bread"));
    }

    @Test
    void getProductById() throws Exception {
        mvc.perform(get("/api/public/product/1").with(user("user")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Milk"))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    void getProductsByUserId() throws Exception {
        mvc.perform(get("/api/public/products/1").with(user("user")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Eggs"))
            .andExpect(jsonPath("$[1].name").value("Milk"));
    }

    @Test
    void addProduct() throws Exception {

        JSONObject productJson = new JSONObject();
        productJson.put("id", 1);
        productJson.put("name", "Butter");
        productJson.put("userId", 1);
        productJson.put("categoryName", "Meal");

        mvc.perform(post("/api/public/products").with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson.toString()))
                .andExpect(status().isOk());

        mvc.perform(get("/api/public/product/1").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Butter"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }


    @Test
    void deleteProduct() throws Exception {
        mvc.perform(get("/api/public/product/2").with(user("user")))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/public/products/2").with(user("user")))
                .andExpect(status().isOk());

        mvc.perform(get("/api/public/product/2").with(user("user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProduct() throws Exception {
        mvc.perform(get("/api/public/product/2").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Eggs"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));

        JSONObject productJson = new JSONObject();
        productJson.put("id", 2);
        productJson.put("name", "Butter");
        productJson.put("userId", 1);
        productJson.put("categoryName", "Meal");

        mvc.perform(put("/api/public/products/2").with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson.toString()))
                .andExpect(status().isOk());

        mvc.perform(get("/api/public/product/2").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Butter"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    void paginateProductsByUserIdByTradeIdIsNotNull() throws Exception {
        mvc.perform(get("/api/public/products3?page=0&orderBy=id&userId=1").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Eggs"));
    }

    @Test
    void paginateProductsByTradeIdIsNotNull() throws Exception {
        mvc.perform(get("/api/public/products4?page=0&orderBy=id").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Bread"))
                .andExpect(jsonPath("$[1].name").value("Eggs"));
    }
}