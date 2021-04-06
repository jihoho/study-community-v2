package com.project.pagu.member.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.pagu.member.service.MemberService;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 4:08
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MemberSaveRequestDtoTest {

    private static final Logger logger = LoggerFactory.getLogger(MemberSaveRequestDtoTest.class);
    private MemberSaveRequestDto dto;
    @Autowired
    private Validator validator;

    @MockBean
    MemberService memberService;

    @BeforeEach
    @DisplayName("MeberSaveRequestDto 유효한 데이터 초기 세팅")
    void beforeEach() {
        dto = MemberSaveRequestDto.builder()
                .email("123@email.com")
                .nickname("nick")
                .password("abcde1234!")
                .passwordCheck("abcde1234!")
                .authKey("123456")
                .authKeyInput("123456")
                .build();
    }

    @Test
    @DisplayName("회원가입 Form validation 성공 테스트")
    void memberFormValidationSuccessTest() throws Exception {
        // when
        when(memberService.existsById(any())).thenReturn(false); // 이메일 unique
        when(memberService.existsByNickname(any())).thenReturn(false); // 닉네임 unique
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);
        // then
        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("회원 이메일 중복 체크 실패 테스트")
    void uniqueEmailValidationFailTest() {
        // when
        when(memberService.existsById(any())).thenReturn(true); // 이메일 중복
        when(memberService.existsByNickname(any())).thenReturn(false); //닉네임 unique
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // then
        ConstraintViolation<MemberSaveRequestDto> violation = violations.iterator().next();
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("email", violation.getPropertyPath().toString()),
                () -> assertEquals("123@email.com", violation.getInvalidValue())
        );
        logger.info("violation error message : {}", violation.getMessage());
    }

    @Test
    @DisplayName("회원 닉네임 Pattern Validation 실패 테스트")
    void nicknamePatternVaildationFailTest() {
        // when
        when(memberService.existsById(any())).thenReturn(false); // 이메일 unique
        when(memberService.existsByNickname(any())).thenReturn(false); //닉네임 unique
        dto.setNickname("nick*"); // 닉네임에 * 특수문자
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);
        ConstraintViolation<MemberSaveRequestDto> violation = violations.iterator().next();
        // then
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("nick*", violation.getInvalidValue())
        );
        logger.info("violation error message : {}", violation.getMessage());
    }


    @Test
    @DisplayName("이메일 인증 번호 불일치 테스트")
    void emailTokenMismatchTest() throws Exception {
        // when
        dto.setAuthKeyInput("123123");
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);
        ConstraintViolation<MemberSaveRequestDto> violation = violations.iterator().next();
        // then
        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("authKeyInput", violation.getPropertyPath().toString()),
                () -> assertEquals("인증 번호가 다릅니다.", violation.getMessage())
        );
    }


}