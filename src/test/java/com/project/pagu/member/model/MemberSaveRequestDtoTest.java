package com.project.pagu.member.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.pagu.member.service.MemberService;
import com.project.pagu.validation.SignUpValidation;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 1:49 오전
 */
@DisplayName("회원 가입 입력 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberSaveRequestDtoTest {

    @Mock
    private MemberService memberService;
    @InjectMocks
    private SignUpValidation signUpValidation;
    private Errors errors;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final MemberSaveRequestDto dto = new MemberSaveRequestDto();

    @BeforeEach
    @DisplayName("정삭적인 입력 초기 세팅")
    void setSuccessMemberSaveRequestDto() {
        dto.setNickname("tester");
        dto.setEmail("tester@test.com");
        dto.setPassword("12345678a!");
        dto.setPasswordCheck("12345678a!");
        dto.setAuthKey("123456");
        dto.setAuthKeyInput("123456");
        errors = new BeanPropertyBindingResult(dto, "MemberSaveRequestDto");
        when(memberService.existsByMemberId(any())).thenReturn(false); // 이메일 unique
        when(memberService.existsByNickname(any())).thenReturn(false); // 닉네임 unique
    }

    @DisplayName("정상 입력")
    @Test
    void input_success() {
        // when
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // MemberSaveDto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        // then
        assertAll(
                () -> assertEquals(0, violations.size()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @Test
    @DisplayName("이메일 중복 실패 테스트")
    void input_fail_email_unique() throws Exception {
        when(memberService.existsByMemberId(any())).thenReturn(true); // 이메일 중복

        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals(0, violations.size()),
                () -> assertEquals(1, errors.getFieldErrorCount()),
                () -> assertEquals("UniqueEmail", errors.getAllErrors().get(0).getCode()),
                () -> assertTrue(errors.hasFieldErrors("email"))
        );
    }

    @Test
    @DisplayName("닉네임 중복 실패 테스트")
    void input_fail_nickname_unique() throws Exception {
        when(memberService.existsByNickname(any())).thenReturn(true); // 닉네임 중복

        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals(0, violations.size()),
                () -> assertEquals(1, errors.getFieldErrorCount()),
                () -> assertEquals("UniqueNickname", errors.getAllErrors().get(0).getCode()),
                () -> assertTrue(errors.hasFieldErrors("nickname"))
        );
    }

    @Test
    @DisplayName("비밀번호 확인 실패 테스트")
    void input_fail_password_check() throws Exception {
        dto.setPasswordCheck("abcdef123!");

        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals(0, violations.size()),
                () -> assertEquals(1, errors.getFieldErrorCount()),
                () -> assertEquals("NotEqualsPassword", errors.getAllErrors().get(0).getCode()),
                () -> assertTrue(errors.hasFieldErrors("passwordCheck"))
        );
    }

    @Test
    @DisplayName("이메일 인증 실패 테스트")
    void input_fail_email_authkey() throws Exception {
        dto.setAuthKeyInput("111111");

        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals(0, violations.size()),
                () -> assertEquals(1, errors.getFieldErrorCount()),
                () -> assertEquals("NotEqualsAuthKeyInput", errors.getAllErrors().get(0).getCode()),
                () -> assertTrue(errors.hasFieldErrors("authKeyInput"))
        );
    }

    @DisplayName("닉네임 2글자 미만")
    @Test
    void input_fail_nickname_min_length() {
        dto.setNickname("t");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        // then
        assertAll(
                () -> assertEquals("2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.",
                        violation.getMessage()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("t", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())

        );
    }

    @DisplayName("닉네임 8글자 초과")
    @Test
    void input_fail_nickname_max_length() {
        dto.setNickname("tester1234");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.",
                        violation.getMessage()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("tester1234", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("올바르지 않은 이메일 형식")
    @Test
    void input_fail_email() {
        dto.setEmail("test@test");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("올바른 형식의 이메일을 입력해주세요.", violation.getMessage()),
                () -> assertEquals("email", violation.getPropertyPath().toString()),
                () -> assertEquals("test@test", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("8글자 미만 비빌번호")
    @Test
    void input_fail_password_min_length() {
        dto.setPassword("1234567");
        dto.setPasswordCheck("1234567");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("1234567", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("20글자 초과 비빌번호")
    @Test
    void input_fail_password_max_length() {
        dto.setPassword("123456789012345678901");
        dto.setPasswordCheck("123456789012345678901");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("123456789012345678901", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("특수문자 미포함 비빌번호")
    @Test
    void input_fail_password_not_special_char() {
        dto.setPassword("12345678a");
        dto.setPasswordCheck("12345678a");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("12345678a", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("숫자 미포함 비빌번호")
    @Test
    void input_fail_password_not_number() {
        dto.setPassword("aaaaaaaa!");
        dto.setPasswordCheck("aaaaaaaa!");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("aaaaaaaa!", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @DisplayName("영문 미포함 비빌번호")
    @Test
    void input_fail_password_not_word() {
        dto.setPassword("12345678!");
        dto.setPasswordCheck("12345678!");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("12345678!", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    @Test
    @DisplayName("닉네임에 특수문자 포함")
    void invalid_nickname_by_special_word() {
        dto.setNickname("nick*"); // 닉네임에 * 특수문자

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        // dto cumstom validation
        signUpValidation.validate(dto, errors);
        signUpValidation.validateEmailAuth(dto.getAuthKey(), dto.getAuthKeyInput(), errors);

        // then
        assertAll(
                () -> assertEquals("2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.",
                        violation.getMessage()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("nick*", violation.getInvalidValue()),
                () -> assertFalse(errors.hasErrors())
        );
    }

    private ConstraintViolation<MemberSaveRequestDto> getViolation() {
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator
                .validate(dto);

        return violations.iterator().next();
    }
}
