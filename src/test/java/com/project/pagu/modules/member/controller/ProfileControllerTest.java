package com.project.pagu.modules.member.controller;

import static com.project.pagu.util.MultiValueMapConverter.convert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.mockMember.WithMember;
import com.project.pagu.modules.member.model.PasswordSaveDto;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

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

    @MockBean
    FileManager fileManager;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("권한이 없는 사용자가 프로필 페이지로 이동 시 로그인 페이지로 redirect 된다.")
    void get_profile_fail() throws Exception {
        mockMvc.perform(get("/members/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 유저인 경우 프로필 수정 폼으로 이동한다.")
    @WithMember
    void profile_update_form() throws Exception {
        memberRepository.save(givenMember());

        mockMvc.perform(get("/members/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/profile"))
                .andExpect(model().attributeExists("profileDto"))
                .andDo(print());
    }

    @WithMember
    @Test
    @DisplayName("프로필 수정에 성공한다.")
    void profile_update() throws Exception {
        memberRepository.save(givenMember());
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        mockMvc.perform(post("/members/profile")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/profile"));
    }

    @WithMember
    @Test
    @DisplayName("잘못된 입력시 프로필 수정에 실패하고 프로필 수정페이지로 이동한다.")
    void profile_update_fail() throws Exception {
        ProfileDto dto = givenDto();
        dto.setChangeNickname("1");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        mockMvc.perform(post("/members/profile")
                .with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/profile"));
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
        memberRepository.save(givenMember());
        mockMvc.perform(get("/profile/{nickname}", "tester"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    @DisplayName("비밀번호 변경을 위해 비밀번호 확인 페이지로 이동한다.")
    void password_check_for_change_password() throws Exception {
        mockMvc.perform(get("/members/password-check/{viewName}", "change-password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("view"))
                .andExpect(model().attributeExists("signUpDto"))
                .andExpect(view().name("members/password-check"));
    }

    @Test
    @DisplayName("없는 페이지로 비밀번호 인증을 요청할겅우 에러페이지로 이동한다.")
    @WithMember
    void password_check_invalid_view_name() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setPassword("123123a!");

        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(post("/members/password-check/{viewName}", "noooo")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("비밀번호가 일치할경우 비밀번호 변경 페이지로 이동한다.")
    @WithMember
    void password_check_success_change_password() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setPassword("123123a!");

        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(post("/members/password-check/{viewName}", "change-password")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/change-password"));
    }

    @Test
    @DisplayName("비밀번호 변경 페이지로 이동한다.")
    void change_password() throws Exception {
        mockMvc.perform(get("/members/change-password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("passwordSaveDto"))
                .andExpect(view().name("members/change-password"));
    }

    @Test
    @WithMember
    @DisplayName("성공적으로 비밀번호를 변경한다.")
    void success_password_change() throws Exception {
        PasswordSaveDto passwordSaveDto = new PasswordSaveDto();
        passwordSaveDto.setPassword("123123aa!");
        passwordSaveDto.setPasswordCheck("123123aa!");

        memberRepository.save(givenMember());

        MultiValueMap<String, String> params = convert(objectMapper, passwordSaveDto);

        mockMvc.perform(post("/members/change-password")
                .with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/password-success"));
    }

    @Test
    @WithMember
    @DisplayName("비밀번호 변경시 비밀번호가 일치하지 않아 비밀번호 입력페이지로 이동한다.")
    void fail_password_change() throws Exception {
        PasswordSaveDto passwordSaveDto = new PasswordSaveDto();
        passwordSaveDto.setPassword("123123aa!");
        passwordSaveDto.setPasswordCheck("123123!!");

        memberRepository.save(givenMember());

        MultiValueMap<String, String> params = convert(objectMapper, passwordSaveDto);

        mockMvc.perform(post("/members/change-password")
                .with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/change-password"));
    }

    @Test
    @DisplayName("회원탈퇴를 위해 비밀번호 확인 페이지로 이동한다.")
    void password_check_secession_member() throws Exception {
        mockMvc.perform(get("/members/password-check/{viewName}", "secession"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("view"))
                .andExpect(model().attributeExists("signUpDto"))
                .andExpect(view().name("members/password-check"));
    }

    @Test
    @DisplayName("비밀번호가 일치할경우 회원탈퇴에 성공한다.")
    @WithMember("tester3")
    void password_check_success_secession() throws Exception {
        Member member = Member.builder()
                .email("tester3@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester3")
                .role(Role.GUEST)
                .imageFilename(null)
                .career("취준생")
                .postion("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();

        memberRepository.save(member);
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setPassword("123123a!");

        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(post("/members/password-check/{viewName}", "secession")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/delete-success"));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않을 경우 비밀번호 입력페이지로 이동한다.")
    @WithMember
    void password_check_fail() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setPassword("different-password!");

        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(post("/members/password-check/{viewName}", "secession")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("view"))
                .andExpect(view().name("members/password-check"));
    }

    @Test
    @DisplayName("회원탈퇴 성공페이지로 이동한다.")
    void secession_success() throws Exception {
        mockMvc.perform(get("/members/delete-success"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/delete-success"));
    }

    @Test
    @DisplayName("프로필에서 게시물 관리 페이지로 이동한다.")
    @WithMember
    void profile_boards() throws Exception {
        mockMvc.perform(get("/profile/boards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boards"))
                .andExpect(view().name("profile/boards"));
    }

    @Test
    @DisplayName("프로필 썸네일을 요청한다.")
    void profile_thumbnails() throws Exception {
        mockMvc.perform(get("/profileThumbnails/{type}/{email}/{filename}",
                "NORMAL", "test@email.com", "file"))
                .andDo(print())
                .andExpect(status().isOk());

        then(fileManager).should().profileThumbnails(any(), any(), any(), any());
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