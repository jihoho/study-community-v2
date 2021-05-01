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
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
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
 * Date: 2021/04/20 Time: 3:41 오후
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
    @DisplayName("회원 가입 페이지로 이동한다.")
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
        MemberSaveRequestDto dto = givenDto();
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
    @DisplayName("가입하는 이메일이 중복일 경우 다시 가입창으로 이동한다.")
    void uniqueEmailValidationFailTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

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
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

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
    @DisplayName("이메일 인증 성공 후 sign-up-success 페이지로 이동")
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
    @DisplayName("이메일 인증이 실패하는 경우 페이지가 유지된다.")
    void emailCheckFailTest() throws Exception {
        MemberSaveRequestDto dto = givenDto();
        dto.setAuthKeyInput("123123");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

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

    @DisplayName("로그인에 실패한다.")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login-process")
                .param("email", "yy123@email.com")      // 존재하지 않는 회원
                .param("password", "dif12314!")
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

    public MemberSaveRequestDto givenDto() {
        MemberSaveRequestDto givenDto = new MemberSaveRequestDto();
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
                .career("취준생")
                .postion("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }
}