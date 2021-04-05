package com.project.pagu.web.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.pagu.domain.email.EmailAuthKey;
import com.project.pagu.service.email.EmailAuthKeyService;
import com.project.pagu.service.members.MembersService;
import java.time.LocalDateTime;
import java.util.Optional;
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
 * Date: 2021-04-05 Time: 오후 10:51
 */

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmailAuthKeyDtoTest {

    private static final Logger logger = LoggerFactory.getLogger(MemberSaveRequestDtoTest.class);
    private EmailAuthKey entity;
    private EmailAuthKeyDto dto;
    @Autowired
    private Validator validator;

    @MockBean
    EmailAuthKeyService emailAuthKeyService;

    @BeforeEach
    @DisplayName("이메일 인증 엔티티 및 dto 세팅")
    void beforeEach() {
        // findById로 조회 했을 경우 return 할 entity
        entity = EmailAuthKey.builder()
                .email("123@email.com")
                .authKey(
                        "$2a$10$7rOasVCj2XtH4x9MD7HzTONunx7qwDU/Rx0ZBgY0H6ueQ3tyyBtzK")// '123456' 암호화 한 값
                .build();
        entity.setModifiedDate(LocalDateTime.now());
        // dto의 authKey의 경우 사용자 입력 값이므로 암호화가 안되어있음.
        dto = EmailAuthKeyDto.builder()
                .email("123@email.com")
                .authKey("123456")
                .build();

        // Email token 조회 시 entity return
        when(emailAuthKeyService.findById(any())).thenReturn(Optional.of(entity));
    }

    @Test
    @DisplayName("이메일 인증 성공 테스트")
    void emailTokenValidationSuccessTest() throws Exception {
        // when
        Set<ConstraintViolation<EmailAuthKeyDto>> violations = validator.validate(dto);
        // then
        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("이메일 인증 번호 불일치 테스트")
    void emailTokenMismatchTest() throws Exception {
        // when
        dto.setAuthKey("123123");
        Set<ConstraintViolation<EmailAuthKeyDto>> violations = validator.validate(dto);
        ConstraintViolation<EmailAuthKeyDto> violation = violations.iterator().next();
        // then
        assertAll(
                ()-> assertEquals(1,violations.size()),
                ()-> assertEquals("authKey",violation.getPropertyPath().toString()),
                ()-> assertEquals("인증 번호가 다릅니다.",violation.getMessage())
        );
    }

    @Test
    @DisplayName("이메일 인증 시간 만료 테스트")
    void emailTokenTimeoutTest() throws Exception {
        // when
        // Email 토큰 30분 전에 등록
        entity.setModifiedDate(LocalDateTime.now().minusMinutes(31));
        Set<ConstraintViolation<EmailAuthKeyDto>> violations = validator.validate(dto);
        ConstraintViolation<EmailAuthKeyDto> violation = violations.iterator().next();
        // then
        assertAll(
                ()-> assertEquals(1,violations.size()),
                ()-> assertEquals("authKey",violation.getPropertyPath().toString()),
                ()-> assertTrue(violation.getMessage().contains("인증 시간"))
        );
    }
}