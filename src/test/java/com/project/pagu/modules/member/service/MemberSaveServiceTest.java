package com.project.pagu.modules.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/08 Time: 10:48 오전
 */

@ExtendWith(MockitoExtension.class)
public class MemberSaveServiceTest {

    @InjectMocks
    private MemberSaveServiceImpl memberSaveService;

    @Mock
    private MemberRepository memberRepository;

    @Captor
    private ArgumentCaptor<Member> argumentCaptor;

    private SignUpDto dto;

    @BeforeEach
    @DisplayName("MemberSaveRequestDto 유효한 데이터 초기 세팅")
    void beforeEach() {
        dto = new SignUpDto();
        dto.setEmail("123@email.com");
        dto.setNickname("nick");
        dto.setPassword("abcde1234!");
        dto.setPasswordCheck("abcde1234!");
        dto.setAuthKey("123456");
        dto.setAuthKeyInput("123456");
    }

    @Test
    @DisplayName("회원 저장")
    void save_member() {
        // given
        given(memberRepository.save(any())).willReturn(dto.toEntity());

        // when
        memberSaveService.saveMember(dto);

        // then
        verify(memberRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getEmail()).isEqualTo("123@email.com");
        then(argumentCaptor.getValue().getNickname()).isEqualTo("nick");
    }

}
