package com.project.pagu.web.members;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pagu.service.util.MultiValueMapConverter;
import com.project.pagu.web.dto.EmailAuthKeyDto;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:07 오후
 */

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@WebMvcTest
class MembersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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


    @Test
    @DisplayName("비밀번호 패턴 validation 실패 테스트")
    void passwordPatternFailTest() throws Exception {
        // given: 비밀번호 특수문자 미포함 invalid
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("123@naver.com")
                        .nickname("nick")
                        .password("123123")
                        .passwordCheck("123123")
                        .build();

        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto","password","Pattern"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동


    }
    
    @Test
    @DisplayName("비밀번호 확인 validation 실패 테스트")
    void passwordCheckFailTest () throws Exception{
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("123@naver.com")
                        .nickname("nick")
                        .password("abcde1234!")
                        .passwordCheck("123123")
                        .build();

        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto","passwordCheck","FieldsValueMatch"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동
    }

    @Test
    @DisplayName("닉네임 패턴 validation 실패 테스트")
    void nicknamePatternFailTest () throws Exception{
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("123@naver.com")
                        .nickname("nick")
                        .password("abcde1234!")
                        .passwordCheck("123123")
                        .build();

        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("memberSaveRequestDto","nickname","FieldsValueMatch"))
                .andExpect(view().name("sign-up")); // 다시 가입창으로 이동
    }
    

    @Test
    @DisplayName("회원가입 Form validation 성공 테스트")
    void formValidationSuccessTest() throws Exception {
        // given: 모든 필드 valid
        MemberSaveRequestDto dto =
                MemberSaveRequestDto.builder()
                        .email("email@email.com")
                        .nickname("nick")
                        .password("ab123456!")
                        .passwordCheck("ab123456!")
                        .build();

        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, dto);
        mockMvc.perform(post("/members/valid").with(csrf())
                .params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("email-check")); // 다시 가입창으로 이동
    }



}