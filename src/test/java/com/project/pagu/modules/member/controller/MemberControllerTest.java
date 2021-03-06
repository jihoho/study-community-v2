package com.project.pagu.modules.member.controller;

import static com.project.pagu.util.MultiValueMapConverter.convert;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.modules.member.domain.UserMember;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import com.project.pagu.modules.member.service.MemberSaveServiceImpl;
import com.project.pagu.modules.member.service.MemberViewService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:07 ??????
 */

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberSaveServiceImpl memberService;

    @MockBean
    private MemberViewService memberViewService;

    private SignUpDto memberSaveDto;

    @Mock
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @DisplayName("memberSaveDto ?????? ?????? ??????")
    void beforeEach() {
        memberSaveDto = new SignUpDto();
        memberSaveDto.setEmail("yy123@email.com");
        memberSaveDto.setNickname("nick");
        memberSaveDto.setPassword("abcde1234!");
        memberSaveDto.setPasswordCheck("abcde1234!");
        memberSaveDto.setAuthKey("123456");
        memberSaveDto.setAuthKeyInput("123456");
    }

    @DisplayName("?????? ?????? ???????????? ????????????.")
    @Test
    void sign_up() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(content().string(containsString("?????????")))
                .andExpect(content().string(containsString("?????????")))
                .andExpect(content().string(containsString("????????????")))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? Form validation ?????? ?????????")
    void formValidationSuccessTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/email-check")); // email-check ????????? ??????
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ?????? ?????? ??????????????? ????????????.")
    void uniqueEmailValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        // when
        when(memberViewService.existsById(any())).thenReturn(true);

        // then
        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "email",
                        "UniqueEmail"))
                .andExpect(content().string(containsString("?????? ???????????? ??????????????????.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ?????? ?????? ??????????????? ????????????.")
    void uniqueNicknameValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        // when
        when(memberViewService.existsByNickname(any())).thenReturn(true);

        // then
        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "nickname",
                        "UniqueNickname"))
                .andExpect(content().string(containsString("?????? ???????????? ??????????????????.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("??????????????? ????????? ????????? ??? ?????? ?????? ?????? ??????????????? ????????????.")
    void nicknamePatternFailTest() throws Exception {
        // given
        memberSaveDto.setNickname("nick&*");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "nickname",
                        "Pattern"))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("??????????????? ???????????? ????????? ??? ?????? ?????? ?????? ??????????????? ????????????.")
    void passwordPatternFailTest() throws Exception {
        // given
        memberSaveDto.setPassword("123");
        memberSaveDto.setPasswordCheck("123");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "password",
                        "Pattern"))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("??????????????? ??????????????? ???????????? ????????? ?????? ?????? ?????? ??????????????? ????????????.")
    void passwordCheckFailTest() throws Exception {
        // given
        memberSaveDto.setPasswordCheck("differentPassword12!");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "passwordCheck",
                        "NotEqualsPassword"))
                .andExpect(content().string(containsString("??????????????? ????????????.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ?????????????????? ????????? ?????? ?????????????????? ????????????.")
    void wrong_approach_email_check() throws Exception {
        memberSaveDto.setEmail("");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);
        // then
        mockMvc.perform(get("/email-check")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("error")); // ????????? ?????? ?????? ??? profile ???????????? ??????
    }


    @DisplayName("????????? ?????? ?????? ??? sign-up-success ???????????? ??????")
    void emailCheckSuccessTest() throws Exception {
        // when
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);
        // then
        mockMvc.perform(post("/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/sign-up-success")); // ????????? ?????? ?????? ??? profile ???????????? ??????
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? ???????????? ????????????.")
    void emailCheckFailTest() throws Exception {
        // given
        memberSaveDto.setAuthKeyInput("123123");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "authKeyInput",
                        "NotEqualsAuthKeyInput"))
                .andExpect(view().name("email-check"));
    }

    @DisplayName("????????? ?????? ???????????? ????????? ???????????? ?????? ??? ????????? ???????????? redirect ??????.")
    @Test
    void get_profile_fail() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andDo(print());
    }

    @DisplayName("???????????? ????????? ????????? ????????? ????????? ????????? ???????????? ??????.")
    @Test
    void not_login_profile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andDo(print());
    }

    @DisplayName("????????? ???????????? ????????????.")
    @Test
    void get_login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("?????????")))
                .andExpect(content().string(containsString("????????????")))
                .andExpect(content().string(containsString("?????????")))
                .andExpect(content().string(containsString("?????? ?????????")))
                .andExpect(content().string(containsString("???????????? ??????")))
                .andExpect(content().string(containsString("????????????")))
                .andDo(print());
    }

    @DisplayName("???????????? ????????????.")
    @Test
    void login_success() throws Exception {
        String encode = passwordEncoder.encode(memberSaveDto.getPassword());
        given(memberViewService.loadUserByUsername(any())).willReturn(new User("yy123@email.com", encode,
                List.of(new SimpleGrantedAuthority("ROLE_GUEST"))));

        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/login-process")
                .params(params)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("yy123@email.com"))
                .andDo(print());
    }

    @DisplayName("???????????? ????????????.")
    @Test
    void login_fail() throws Exception {
        memberSaveDto.setPassword(passwordEncoder.encode(memberSaveDto.getPassword()));
        given(memberViewService.loadUserByUsername(anyString()))
                .willReturn(new UserMember(memberSaveDto.toEntity()));
        mockMvc.perform(post("/login-process")
                .param("email", "yy123@email.com")
                .param("password", "dif12314!") // ?????? ????????????(abcde1234!) ??? ?????? ????????????
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("????????? ???????????? ????????? ????????? ????????? ?????????????????? ????????????.")
    @Test
    @WithMockUser
    void login_status_try_login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andExpect(authenticated());
    }

    @Test
    void test() {
        String ss = "Sdjaldjakldjl";
        String aa = "Sdjaldjakldjl";
        passwordEncoder.equals(ss);
        passwordEncoder.equals(ss);
        System.out.println(passwordEncoder.matches(aa, ss));

    }

}
