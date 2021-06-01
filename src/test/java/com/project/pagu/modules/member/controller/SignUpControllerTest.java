package com.project.pagu.modules.member.controller;

import static com.project.pagu.util.MultiValueMapConverter.convert;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.project.pagu.modules.member.mockMember.WithMember;
import com.project.pagu.modules.member.model.OauthSaveDto;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
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
class SignUpControllerTest {

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

    @MockBean
    JavaMailSender javaMailSender;

    @Test
    @DisplayName("회원 가입 페이지로 이동한다.")
    void sign_up() throws Exception {
        mockMvc.perform(get("/members/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/sign-up"))
                .andExpect(content().string(containsString("이메일")))
                .andExpect(content().string(containsString("닉네임")))
                .andExpect(content().string(containsString("비밀번호")))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 Form validation 성공 테스트")
    void formValidationSuccessTest() throws Exception {
        SignUpDto dto = givenDto();
        dto.setEmail("other@email.com");
        dto.setNickname("other-id");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        mockMvc.perform(post("/members/sign-up")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/members/sign-up/email-check"));
    }

    @Test
    @DisplayName("가입하는 이메일이 중복일 경우 다시 가입창으로 이동한다.")
    void uniqueEmailValidationFailTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

        mockMvc.perform(post("/members/sign-up")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "email",
                        "UniqueEmail"))
                .andExpect(content().string(containsString("이미 존재하는 이메일입니다.")))
                .andExpect(view().name("members/sign-up"));
    }

    @Test
    @DisplayName("가입하는 닉네임이 중복일 경우 다시 가입창으로 이동한다.")
    void uniqueNicknameValidationFailTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        memberRepository.save(givenMember());

        mockMvc.perform(post("/members/sign-up")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "nickname",
                        "UniqueNickname"))
                .andExpect(content().string(containsString("이미 존재하는 닉네임입니다.")))
                .andExpect(view().name("members/sign-up"));
    }

    @Test
    @DisplayName("이메일 인증 페이지로 이동한다.")
    void signup_email_check() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("tester@test.com");
        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(get("/members/sign-up/email-check")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("members/sign-up/email-check"));
    }

    @Test
    @DisplayName("구글 가입 페이지로 이동한다.")
    void sign_up_google() throws Exception {
        mockMvc.perform(get("/members/sign-up/google"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("oauthSaveDto"))
                .andExpect(view().name("members/sign-up/google"));
    }

    @Test
    @WithMember
    @DisplayName("구글 계정 가입에 성공한다.")
    void sign_up_google_success() throws Exception {
        Member member = Member.builder()
                .email("google@gmail.com")
                .memberType(MemberType.GOOGLE)
                .role(Role.GUEST)
                .build();
        memberRepository.save(member);

        OauthSaveDto oauthSaveDto = new OauthSaveDto();
        oauthSaveDto.setNickname("google");

        MultiValueMap<String, String> params = convert(objectMapper, oauthSaveDto);

        mockMvc.perform(post("/members/sign-up/google")
                .with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMember
    @DisplayName("구글 계정 가입에 실패한다.")
    void sign_up_google_fail() throws Exception {
        Member member = Member.builder()
                .email("google@gmail.com")
                .memberType(MemberType.GOOGLE)
                .role(Role.GUEST)
                .build();
        memberRepository.save(member);

        OauthSaveDto oauthSaveDto = new OauthSaveDto();
        oauthSaveDto.setNickname("NooooooooooooooName");

        MultiValueMap<String, String> params = convert(objectMapper, oauthSaveDto);

        mockMvc.perform(post("/members/sign-up/google")
                .with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/sign-up/google"));
    }

    @Test
    @DisplayName("회원가입 이메일 인증시 이메일이 없는 경우 에러페이지로 이동한다.")
    void email_isEmpty() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("");
        MultiValueMap<String, String> params = convert(objectMapper, signUpDto);

        mockMvc.perform(get("/members/sign-up/email-check")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("이메일 인증 성공 후 회원가입 성공 페이지로 이동")
    void emailCheckSuccessTest() throws Exception {
        MultiValueMap<String, String> params = convert(objectMapper, givenDto());

        mockMvc.perform(post("/members/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("redirect:/members/sign-up/success"));
    }

    @Test
    @DisplayName("이메일 인증이 실패하는 경우 페이지가 유지된다.")
    void emailCheckFailTest() throws Exception {
        SignUpDto dto = givenDto();
        dto.setAuthKeyInput("123123");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        mockMvc.perform(post("/members/sign-up/email-check")
                .with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "signUpDto",
                        "authKeyInput",
                        "NotEqualsAuthKeyInput"))
                .andExpect(view().name("members/sign-up/email-check"));
    }

    @Test
    @DisplayName("회원가입에 성공페이지로 이동한다.")
    void signup_success() throws Exception {
        mockMvc.perform(get("/members/sign-up/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/sign-up/success"));
    }

    @Test
    @DisplayName("비밀번호 찾기 페이지를 요청받으면 이메일 입력 페이지로 이동한다.")
    void password_find_form() throws Exception {
        mockMvc.perform(get("/members/password-find"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpDto"))
                .andExpect(view().name("members/password-find"));
    }

    @Test
    @DisplayName("정상적인 이메일을 입력받고 이메일 인증 페이지로 이동한다.")
    void submit_email() throws Exception {
        memberRepository.save(givenDto().toEntity());

        mockMvc.perform(post("/members/password-find")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/email-check"));
    }

    @Test
    @DisplayName("비정상적인 이메일을 입력받고 이메일 인증 페이지로 이동한다.")
    void invalid_submit_email() throws Exception {
        mockMvc.perform(post("/members/password-find")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/members/email-check"));
    }

    @Test
    @DisplayName("이메일 인증 페이지로 이동한다.")
    void email_check() throws Exception {
        mockMvc.perform(get("/members/email-check")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/email-check"));
    }

    @Test
    @DisplayName("이메일 인증이 완료되면 새로운 비밀번호로 변경한다.")
    void send_email_authKey() throws Exception {
        SignUpDto dto = givenDto();
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        memberRepository.save(dto.toEntity());

        mockMvc.perform(post("/members/email-check")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        Member member = memberRepository.findByNickname("tester").get();

        assertFalse(passwordEncoder.matches(member.getPassword(), dto.getPassword()));
    }

    @Test
    @DisplayName("이메일 인증에 실패하면 이메일 인증 페이지로 이동한다.")
    void send_email_authKey_fail() throws Exception {
        SignUpDto dto = givenDto();
        dto.setAuthKeyInput("nooooooo");
        MultiValueMap<String, String> params = convert(objectMapper, dto);

        memberRepository.save(dto.toEntity());

        mockMvc.perform(post("/members/email-check")
                .params(params)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/email-check"));
    }

    @Test
    @DisplayName("비밀번호 변경 성공페이지로 이동한다.")
    void password_change_success() throws Exception {
        mockMvc.perform(get("/members/password-success"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("members/password-success"));
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
                .career("취준생")
                .position("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }
}