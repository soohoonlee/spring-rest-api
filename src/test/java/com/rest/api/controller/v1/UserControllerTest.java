package com.rest.api.controller.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        signUp();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "1234");

        MvcResult mvcResult = mockMvc.perform(post("/v1/sign-in")
                    .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        token = jacksonJsonParser.parseMap(contentAsString).get("data").toString();
    }

    private void signUp() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "1234");
        params.add("name", "test");
        mockMvc.perform(post("/v1/sign-up")
                    .params(params));
    }

    @Test
    void invalidToken() throws Exception {
        mockMvc.perform(get("/v1/users")
                    .header("X-AUTH-TOKEN", "XXXXX"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @WithMockUser(username = "mockUser", roles = {"ADMIN"})
    @Test
    void accessDenied() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void findAllUsers() throws Exception {
        mockMvc.perform(get("/v1/users")
                    .header("X-AUTH-TOKEN", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.list").exists());
    }

    @Test
    void findUser() throws Exception {
        mockMvc.perform(get("/v1/user")
                    .header("X-AUTH-TOKEN", token)
                    .param("lang", "ko"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }
}