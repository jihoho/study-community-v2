package com.project.pagu.modules.member.controller;

import static com.project.pagu.util.MultiValueMapConverter.convert;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.modules.member.mockMember.WithMember;
import com.project.pagu.modules.member.model.ProfileRequestDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/20 Time: 4:08 오후
 */

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @DisplayName("권한이 없는 사용자가 프로필 페이지로 이동 시 로그인 페이지로 redirect 된다.")
    @Test
    void get_profile_fail() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andDo(print());
    }

    @DisplayName("비로그인 상태로 프로필 페이지 접근시 로그인 페이지로 이동.")
    @Test
    void not_login_profile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 수정 폼으로 이동한다.")
    @WithMember
    void profile_update_form() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("profileRequestDto"))
                .andDo(print());
    }

    @WithMember
    @Test
    @DisplayName("프로필 수정에 성공한다.")
    void profile_update() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        mockMvc.perform(post("/members/update")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection());
    }

    private ProfileRequestDto givenDto() {
        return ProfileRequestDto.builder()
                .nickname("tester")
                .email("test@email.com")
                .memberType("NORMAL")
                .nickname("tester")
                .changeNickname("nickname")
                .career("취준생")
                .position("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }

}