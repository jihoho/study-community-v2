package com.project.pagu.board.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 11:11 오전
 */

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("메인페이지로 이동한다.")
    @Test
    void move_main_page() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main-body"))
                .andExpect(content().string(containsString("PAGU")))
                .andExpect(content().string(containsString("스터디 에티켓")))
                .andExpect(content().string(containsString("최근 모집 공고")))
                .andExpect(content().string(containsString("스터디 참여 방법")))
                .andExpect(content().string(containsString("스터디 모집 공고 올리는 법")))
                .andDo(print());
    }

    @DisplayName("회원 가입 페이지로 이동한다.")
    @Test
    void sign_up() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(content().string(containsString("이메일")))
                .andExpect(content().string(containsString("닉네임")))
                .andExpect(content().string(containsString("비밀번호")))
                .andDo(print());
    }

    @DisplayName("로그인 페이지로 이동한다.")
    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("이메일")))
                .andExpect(content().string(containsString("비밀번호")))
                .andExpect(content().string(containsString("로그인")))
                .andExpect(content().string(containsString("구글 로그인")))
                .andExpect(content().string(containsString("비밀번호 찾기")))
                .andExpect(content().string(containsString("회원가입")))
                .andDo(print());
    }
}