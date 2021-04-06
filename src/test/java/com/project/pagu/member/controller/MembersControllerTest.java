package com.project.pagu.member.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.signup.domain.EmailAuthKey;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.signup.service.EmailAuthKeyService;
import com.project.pagu.member.service.MembersService;
import com.project.pagu.util.MultiValueMapConverter;
import com.project.pagu.signup.model.EmailAuthKeyDto;
import com.project.pagu.member.model.MemberSaveRequestDto;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:07 오후
 */

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
        //@WebMvcTest
class MembersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MembersService membersService;
    @MockBean
    private EmailAuthKeyService emailAuthKeyService;

    private MockHttpSession httpSession = new MockHttpSession();
    private MemberSaveRequestDto dto;

    @DisplayName("프로필 페이지로 이동한다.")
    @Test
    void login() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString("프로필 정보")))
                .andExpect(content().string(containsString("이메일")))
                .andExpect(content().string(containsString("닉네임")))
                .andExpect(content().string(containsString("포지션")))
                .andExpect(content().string(containsString("경력")))
                .andExpect(content().string(containsString("링크")))
                .andDo(print());
    }

    @BeforeEach
    void beforeEach() {
        // given: 테스트마다 dto set
        dto = MemberSaveRequestDto.builder()
                .email("123@naver.com")
                .nickname("nick")
                .password("abcde1234!")
                .passwordCheck("abcde1234!")
                .build();
    }

    @Test
    @DisplayName("회원 이메일 중복 체크 실패 테스트")
    void uniqueEmailValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        // when
        when(membersService.existsById(any())).thenReturn(true);
        // then
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto", "email",
                        "UniqueEmail"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동

    }

    @Test
    @DisplayName("회원 닉네임 중복 체크 실패 테스트")
    void uniqueNicknameValidationFailTest() throws Exception {
        // given
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        // when
        when(membersService.existsByNickname(any())).thenReturn(true);
        // then
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto", "nickname",
                        "UniqueNickname"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동

    }


    @Test
    @DisplayName("비밀번호 패턴 validation 실패 테스트")
    void passwordPatternFailTest() throws Exception {
        // given: 비밀번호 특수문자 미포함 invalid
        dto.setPassword("123");
        dto.setPasswordCheck("123");
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);

        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto", "password",
                        "Pattern"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동


    }

    @Test
    @DisplayName("비밀번호 확인 validation 실패 테스트")
    void passwordCheckFailTest() throws Exception {
        // given: 비밀번호 확인 'abcde1234!'와 다른 값
        dto.setPasswordCheck("123");
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);

        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(
                        model().attributeHasFieldErrorCode("memberSaveRequestDto", "passwordCheck",
                                "FieldsValueMatch"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동
    }

    @Test
    @DisplayName("닉네임 pattern validation 실패 테스트")
    void nicknamePatternFailTest() throws Exception {
        // given: 닉네임 _-이외의 특수 문자 포함
        dto.setNickname("nick&*");
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);

        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto", "nickname",
                        "Pattern"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동
    }


    @Test
    @DisplayName("회원가입 Form validation 성공 테스트")
    void formValidationSuccessTest() throws Exception {
        // given: 모든 필드 valid
        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        // when
        when(membersService.save(any()))
                .thenReturn(new MemberId(dto.getEmail(), MemberType.NORMAL));
        when(emailAuthKeyService.save(any())).thenReturn(dto.getEmail());

        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("email-check")); // email-check 페이지 이동
    }


    @Test
    @DisplayName("이메일 인증 불일치 테스트")
    void emailCheckFailTest() throws Exception {
        // given
        EmailAuthKeyDto emailAuthKeyDto = EmailAuthKeyDto.builder().email("123@naver.com")
                .authKey("123456").build();
        MultiValueMap<String, String> params = MultiValueMapConverter
                .convert(objectMapper, emailAuthKeyDto);
        // when
        EmailAuthKey emailAuthKey = EmailAuthKey.builder()
                .email(emailAuthKeyDto.getEmail())
                .authKey("diff-auth-key-123asd123asd")
                .build();
        emailAuthKey.setModifiedDate(LocalDateTime.now());
        when(emailAuthKeyService.findById(any())).thenReturn(Optional.of(emailAuthKey));
        httpSession.setAttribute("memberInfo", dto);

        // then
        mockMvc.perform(post("/members/email-check").with(csrf())
                .params(params).session(httpSession))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("emailAuthKeyDto", "authKey",
                        "ValidAuthKey"))
                .andExpect(view().name("email-check")); // 다시 email-check 페이지 이동

    }

    @Test
    @DisplayName("이메일 인증 시간 초과 테스트")
    void emailCheckTimeoutTest() throws Exception {
        // given
        EmailAuthKeyDto emailAuthKeyDto = EmailAuthKeyDto.builder().email("123@naver.com")
                .authKey("123456").build();
        MultiValueMap<String, String> params = MultiValueMapConverter
                .convert(objectMapper, emailAuthKeyDto);
        // when
        EmailAuthKey emailAuthKey = EmailAuthKey.builder()
                .email(emailAuthKeyDto.getEmail())
                .authKey("$2a$10$7rOasVCj2XtH4x9MD7HzTONunx7qwDU/Rx0ZBgY0H6ueQ3tyyBtzK")
                .build();
        emailAuthKey.setModifiedDate(LocalDateTime.now().minusMinutes(31)); // 발급 시간 30분 초과
        when(emailAuthKeyService.findById(any())).thenReturn(Optional.of(emailAuthKey));
        httpSession.setAttribute("memberInfo", dto);

        // then
        mockMvc.perform(post("/members/email-check").with(csrf())
                .params(params).session(httpSession))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("emailAuthKeyDto", "authKey",
                        "ValidAuthKey"))
                .andExpect(view().name("email-check")); // 다시 email-check 페이지 이동
    }

    @Test
    @DisplayName("이메일 인증 성공 테스트")
    void emailCheckSuccessTest() throws Exception {
        // given
        EmailAuthKeyDto emailAuthKeyDto = EmailAuthKeyDto.builder().email("123@naver.com")
                .authKey("123456").build();
        MultiValueMap<String, String> params = MultiValueMapConverter
                .convert(objectMapper, emailAuthKeyDto);
        // when
        EmailAuthKey emailAuthKey = EmailAuthKey.builder()
                .email(emailAuthKeyDto.getEmail())
                .authKey("$2a$10$7rOasVCj2XtH4x9MD7HzTONunx7qwDU/Rx0ZBgY0H6ueQ3tyyBtzK")
                .build();
        emailAuthKey.setModifiedDate(LocalDateTime.now().minusMinutes(10)); // 발급 시간 유효
        when(emailAuthKeyService.findById(any())).thenReturn(Optional.of(emailAuthKey));
        httpSession.setAttribute("memberInfo", dto);

        // then
        mockMvc.perform(post("/members/email-check").with(csrf())
                .params(params).session(httpSession))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(0))
                .andExpect(view().name("profile")); // 이메일 인증 성공 후 profile 페이지로 이동
    }

}