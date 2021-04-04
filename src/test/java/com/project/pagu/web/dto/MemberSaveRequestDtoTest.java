package com.project.pagu.web.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 4:08
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MemberSaveRequestDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validatorFromFactory;
    private static final Logger logger= LoggerFactory.getLogger(MemberSaveRequestDtoTest.class);

    @Autowired
    private Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validatorFromFactory = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }
    
    @Test
    @DisplayName("회원가입 FORM validation 성공 테스트")
    void memberFormValidationSuccessTest() throws Exception{
        // given
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("123@naver.com")
                        .nickname("nick")
                        .password("abcd1234!")
                        .passwordCheck("abcd1234!")
                        .build();
        // when
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // then
        assertTrue(violations.isEmpty());
    }
    
    @Test
    @DisplayName("validator 테스트")
    void test_should_fail() {
        // Given
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("123@naver.com")
                        .nickname("nick")
                        .password("123123")
                        .passwordCheck("123123")
                        .build();
        // When
        Set<ConstraintViolation<MemberSaveRequestDto>> violations = validator.validate(dto);

        // Then
        assertNotNull(violations);
        for(ConstraintViolation<MemberSaveRequestDto> constraintViolation : violations){
            logger.debug("violation error message : {}", constraintViolation.getMessage());
        }
    }


}