package com.project.pagu.member.controller;

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
import com.project.pagu.member.domain.UserMember;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
import com.project.pagu.member.service.MemberServiceImpl;
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
 * Date: 2021/04/01 Time: 3:07 오후
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
    private MemberServiceImpl memberService;

    private MemberSaveRequestDto memberSaveDto;

    @Mock
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @DisplayName("memberSaveDto 정상 입력 세팅")
    void beforeEach() {
        memberSaveDto = new MemberSaveRequestDto();
        memberSaveDto.setEmail("yy123@email.com");
        memberSaveDto.setNickname("nick");
        memberSaveDto.setPassword("abcde1234!");
        memberSaveDto.setPasswordCheck("abcde1234!");
        memberSaveDto.setAuthKey("123456");
        memberSaveDto.setAuthKeyInput("123456");
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

    @Test
    @DisplayName("회원가입 Form validation 성공 테스트")
    void formValidationSuccessTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/email-check")); // email-check 페이지 이동
    }

    @Test
    @DisplayName("가입하는 이메일이 중복일 경우 다시 가입창으로 이동한다.")
    void uniqueEmailValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        // when
        when(memberService.existsById(any())).thenReturn(true);

        // then
        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "memberSaveRequestDto",
                        "email",
                        "UniqueEmail"))
                .andExpect(content().string(containsString("이미 존재하는 이메일입니다.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("가입하는 닉네임이 중복일 경우 다시 가입창으로 이동한다.")
    void uniqueNicknameValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        // when
        when(memberService.existsByNickname(any())).thenReturn(true);

        // then
        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "memberSaveRequestDto",
                        "nickname",
                        "UniqueNickname"))
                .andExpect(content().string(containsString("이미 존재하는 닉네임입니다.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("회원가입시 닉네임 패턴이 잘 못된 경우 다시 가입창으로 이동한다.")
    void nicknamePatternFailTest() throws Exception {
        // given
        memberSaveDto.setNickname("nick&*");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "memberSaveRequestDto",
                        "nickname",
                        "Pattern"))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("회원가입시 비밀번호 패턴이 잘 못된 경우 다시 가입창으로 이동한다.")
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
                        "memberSaveRequestDto",
                        "password",
                        "Pattern"))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("회원가입시 비밀번호와 비밀번호 확인이 다른 경우 다시 가입창으로 이동한다.")
    void passwordCheckFailTest() throws Exception {
        // given
        memberSaveDto.setPasswordCheck("differentPassword12!");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/valid")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "memberSaveRequestDto",
                        "passwordCheck",
                        "NotEqualsPassword"))
                .andExpect(content().string(containsString("비밀번호가 다릅니다.")))
                .andExpect(view().name("sign-up"));
    }

    @Test
    @DisplayName("이메일입력 없이 이메일 인증페이지로 이동한 경우 에러페이지로 이동한다.")
    void wrong_approach_email_check() throws Exception {
        memberSaveDto.setEmail("");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);
        // then
        mockMvc.perform(get("/email-check")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("error")); // 이메일 인증 성공 후 profile 페이지로 이동
    }


    @DisplayName("이메일 인증 성공 후 sign-up-success 페이지로 이동")
    void emailCheckSuccessTest() throws Exception {
        // when
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);
        // then
        mockMvc.perform(post("/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/sign-up-success")); // 이메일 인증 성공 후 profile 페이지로 이동
    }

    @Test
    @DisplayName("이메일 인증이 실패하는 경우 페이지가 유지된다.")
    void emailCheckFailTest() throws Exception {
        // given
        memberSaveDto.setAuthKeyInput("123123");
        MultiValueMap<String, String> params = convert(objectMapper, memberSaveDto);

        mockMvc.perform(post("/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "memberSaveRequestDto",
                        "authKeyInput",
                        "NotEqualsAuthKeyInput"))
                .andExpect(view().name("email-check"));
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

    @DisplayName("로그인 페이지로 이동한다.")
    @Test
    void get_login() throws Exception {
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

    @DisplayName("로그인에 성공한다.")
    @Test
    void login_success() throws Exception {
        String encode = passwordEncoder.encode(memberSaveDto.getPassword());
        given(memberService.loadUserByUsername(any())).willReturn(new User("yy123@email.com", encode,
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

    @DisplayName("로그인에 실패한다.")
    @Test
    void login_fail() throws Exception {
        memberSaveDto.setPassword(passwordEncoder.encode(memberSaveDto.getPassword()));
        given(memberService.loadUserByUsername(anyString()))
                .willReturn(new UserMember(memberSaveDto.toEntity()));
        mockMvc.perform(post("/login-process")
                .param("email", "yy123@email.com")
                .param("password", "dif12314!") // 기존 비밀번호(abcde1234!) 와 다른 비밀번호
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인 상태에서 로그인 페이지 요청시 에러페이지로 이동한다.")
    @Test
    @WithMockUser
    void login_status_try_login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andExpect(authenticated());
    }

}
