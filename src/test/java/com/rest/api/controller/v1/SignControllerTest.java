package com.rest.api.controller.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName(value = "로그인")
    @Test
    void signIn() throws Exception {
        signUp();
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "1234");

        // when
        ResultActions resultActions = mockMvc.perform(post("/v1/sign-in")
                    .params(params));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName(value = "로그인 실패")
    @Test
    void signInFail() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "12345");

        // when
        ResultActions perform = mockMvc.perform(post("/v1/sign-in").params(params));

        // then
        perform.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1001))
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName(value = "회원 가입")
    @Test
    void signUp() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "1234");
        params.add("name", "test");

        // when
        ResultActions resultActions = mockMvc.perform(post("/v1/sign-up")
                .params(params));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName(value = "회원 가입 실패")
    @Test
    void signUpFail() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "test@gmail.com");
        params.add("password", "1234");

        // when
        ResultActions resultActions = mockMvc.perform(post("/v1/sign-up")
                .params(params));

        // then
        resultActions.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-9999));
    }
}