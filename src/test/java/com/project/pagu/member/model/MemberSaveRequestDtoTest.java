package com.project.pagu.member.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 1:49 오전
 */
@DisplayName("회원 가입 입력 관련 테스트")
public class MemberSaveRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final MemberSaveRequestDto MemberSaveRequestDto = new MemberSaveRequestDto();

    @BeforeEach
    @DisplayName("정삭적인 입력 초기 세팅")
    void setSuccessMemberSaveRequestDto() {
        MemberSaveRequestDto.setNickname("tester");
        MemberSaveRequestDto.setEmail("tester@test.com");
        MemberSaveRequestDto.setPassword("12345678a!");
        MemberSaveRequestDto.setPasswordCheck("12345678a!");
    }

    @DisplayName("정상 입력")
    @Test
    void input_success() {
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(MemberSaveRequestDto);

        assertTrue(violations.isEmpty());
        assertEquals(0, violations.size());
    }

    @DisplayName("닉네임 2글자 미만")
    @Test
    void input_fail_nickname_min_length() {
        MemberSaveRequestDto.setNickname("t");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.", violation.getMessage()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("t", violation.getInvalidValue())
        );
    }

    @DisplayName("닉네임 8글자 초과")
    @Test
    void input_fail_nickname_max_length() {
        MemberSaveRequestDto.setNickname("tester1234");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.", violation.getMessage()),
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("tester1234", violation.getInvalidValue())
        );
    }

    @DisplayName("올바르지 않은 이메일 형식")
    @Test
    void input_fail_email() {
        MemberSaveRequestDto.setEmail("test@test");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("올바른 형식의 이메일을 입력해주세요.", violation.getMessage()),
                () -> assertEquals("email", violation.getPropertyPath().toString()),
                () -> assertEquals("test@test", violation.getInvalidValue())
        );
    }

    @DisplayName("8글자 미만 비빌번호")
    @Test
    void input_fail_password_min_length() {
        MemberSaveRequestDto.setPassword("1234567");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("1234567", violation.getInvalidValue())
        );
    }

    @DisplayName("20글자 초과 비빌번호")
    @Test
    void input_fail_password_max_length() {
        MemberSaveRequestDto.setPassword("123456789012345678901");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("123456789012345678901", violation.getInvalidValue())
        );
    }

    @DisplayName("특수문자 미포함 비빌번호")
    @Test
    void input_fail_password_not_special_char() {
        MemberSaveRequestDto.setPassword("12345678a");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("12345678a", violation.getInvalidValue())
        );
    }

    @DisplayName("숫자 미포함 비빌번호")
    @Test
    void input_fail_password_not_number() {
        MemberSaveRequestDto.setPassword("aaaaaaaa!");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("aaaaaaaa!", violation.getInvalidValue())
        );
    }

    @DisplayName("영문 미포함 비빌번호")
    @Test
    void input_fail_password_not_word() {
        MemberSaveRequestDto.setPassword("12345678!");

        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();

        assertAll(
                () -> assertEquals("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
                        violation.getMessage()),
                () -> assertEquals("password", violation.getPropertyPath().toString()),
                () -> assertEquals("12345678!", violation.getInvalidValue())
        );
    }

    @Test
    @DisplayName("닉네임에 특수문자 포함")
    void invalid_nickname_by_special_word() {
        MemberSaveRequestDto.setNickname("nick*"); // 닉네임에 * 특수문자
        ConstraintViolation<MemberSaveRequestDto> violation = getViolation();
        // then
        assertAll(
                () -> assertEquals("nickname", violation.getPropertyPath().toString()),
                () -> assertEquals("nick*", violation.getInvalidValue())
        );
    }

    private ConstraintViolation<MemberSaveRequestDto> getViolation() {
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator
                .validate(MemberSaveRequestDto);

        return violations.iterator().next();
    }
}
