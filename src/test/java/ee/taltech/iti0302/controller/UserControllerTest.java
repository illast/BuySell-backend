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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends AbstractIntegration {

    @Autowired
    private MockMvc mvc;

    @Test
    void getUsers() throws Exception {
        mvc.perform(get("/api/users").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Snow"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getUserById() throws Exception {
        int userId = 1;
        mvc.perform(get("/api/users/" + userId).with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Snow"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void createUser() throws Exception {

        JSONObject userJson = new JSONObject();
        userJson.put("id", 1);
        userJson.put("firstName", "Jimmy");
        userJson.put("lastName", "Neutron");
        userJson.put("email", "test@mail.ru");
        userJson.put("password", "qwerty");

        mvc.perform(post("/api/public/users").with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.toString()))
                .andExpect(status().isOk());

        mvc.perform(get("/api/users").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].firstName").value("Jimmy"))
                .andExpect(jsonPath("$[1].lastName").value("Neutron"))
                .andExpect(jsonPath("$[1].email").value("test@mail.ru"))
                .andExpect(jsonPath("$[1].id").value(1));
    }

    @Test
    void login() throws Exception {

        JSONObject userJson = new JSONObject();
        userJson.put("id", 1);
        userJson.put("firstName", "Jimmy");
        userJson.put("lastName", "Neutron");
        userJson.put("email", "test@mail.ru");
        userJson.put("password", "qwerty");

        mvc.perform(post("/api/public/users").with(user("user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(status().isOk());

        JSONObject loginRequestJson = new JSONObject();
        loginRequestJson.put("email", "test@mail.ru");
        loginRequestJson.put("password", "qwerty");

        mvc.perform(post("/api/public/login").with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(notNullValue()));
    }

    @Test
    void changeBalance() throws Exception {

        JSONObject userJson = new JSONObject();
        userJson.put("id", 1);
        userJson.put("firstName", "Jimmy");
        userJson.put("lastName", "Neutron");
        userJson.put("email", "test@mail.ru");
        userJson.put("password", "qwerty");

        mvc.perform(post("/api/public/users").with(user("user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(status().isOk());

        JSONObject userBalanceRequestJson = new JSONObject();
        userBalanceRequestJson.put("balance", 100);

        // Set balance to 100.
        int userId = 1;
        mvc.perform(put("/api/users/" + userId).with(user("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userBalanceRequestJson.toString()))
                .andExpect(status().isOk());

        // Expect the balance to be 100.
        mvc.perform(get("/api/users").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].firstName").value("Jimmy"))
                .andExpect(jsonPath("$[1].balance").value(100));
    }
}