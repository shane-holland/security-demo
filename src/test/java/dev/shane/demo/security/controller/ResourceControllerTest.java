package dev.shane.demo.security.controller;

import dev.shane.demo.security.config.SecurityConfig;
import dev.shane.demo.security.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ResourceController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
class ResourceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        final MvcResult result = this.mvc.perform(post("/auth/token")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andReturn();

        final String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world: admin"));

        this.mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void rootWithMockUserStatusIsOk() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }
}