package com.project.pagu.modules.member.controller;

import static com.project.pagu.util.MultiValueMapConverter.convert;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/20 Time: 3:41 ??????
 */
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ?????? ???????????? ????????????.")
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
        SignUpDto dto = givenDto();
        dto.setEmail("other@email.com");
        dto.setNickname("other-id");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/email-check"));
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ?????? ?????? ??????????????? ????????????.")
    void uniqueEmailValidationFailTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

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
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

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
    @DisplayName("????????? ?????? ?????? ??? sign-up-success ???????????? ??????")
    void emailCheckSuccessTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        mockMvc.perform(post("/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/sign-up-success"));
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? ???????????? ????????????.")
    void emailCheckFailTest() throws Exception {
        SignUpDto dto = givenDto();
        dto.setAuthKeyInput("123123");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

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
    @WithMockUser
    void login_success() throws Exception {
        memberRepository.save(givenMember());

        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        mockMvc.perform(post("/login-process")
                .params(params)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("test@email.com"))
                .andDo(print());
    }

    @DisplayName("???????????? ????????????.")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login-process")
                .param("email", "yy123@email.com")      // ???????????? ?????? ??????
                .param("password", "dif12314!")
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

    public SignUpDto givenDto() {
        SignUpDto givenDto = new SignUpDto();
        givenDto.setEmail("test@email.com");
        givenDto.setNickname("tester");
        givenDto.setPassword("abcde1234!");
        givenDto.setPasswordCheck("abcde1234!");
        givenDto.setAuthKey("123456");
        givenDto.setAuthKeyInput("123456");

        return givenDto;
    }

    public Member givenMember() {
        return Member.builder()
                .email("test@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .password(passwordEncoder.encode("abcde1234!"))
                .role(Role.GUEST)
                .imageFilename(null)
                .career("?????????")
                .postion("?????????")
                .link("test@gi.com")
                .info("???????????????")
                .build();
    }
}