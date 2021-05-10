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
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.mockMember.WithMember;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
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
                .andExpect(model().attributeExists("profileDto"))
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

    @Test
    @DisplayName("상대방 프로필 페이지로 이동한다.")
    @Transactional
    @WithMember("tester2")
    void get_profile() throws Exception {
        memberRepository.save(givenMember());
        mockMvc.perform(get("/profile/{nickname}", "tester"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/profile/detail"));
    }

    @Test
    @DisplayName("본인을 조회할 경우 프로필관리 페이지로 이동한다.")
    @WithMember
    void go_to_my_page() throws Exception {
        mockMvc.perform(get("/profile/{nickname}", "tester"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
    }

    private ProfileDto givenDto() {
        return ProfileDto.builder()
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

    public Member givenMember() {
        return Member.builder()
                .email("tester@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .role(Role.GUEST)
                .imageFilename(null)
                .career("취준생")
                .postion("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }

}