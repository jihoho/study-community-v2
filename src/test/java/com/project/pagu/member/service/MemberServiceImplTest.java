package com.project.pagu.member.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.repository.MemberRepository;
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
public class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Captor
    private ArgumentCaptor<Member> argumentCaptor;

    private MemberSaveRequestDto dto;

    @BeforeEach
    @DisplayName("MemberSaveRequestDto 유효한 데이터 초기 세팅")
    void beforeEach() {
        dto = new MemberSaveRequestDto();
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
        memberService.saveMember(dto);

        // then
        verify(memberRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getEmail()).isEqualTo("123@email.com");
        then(argumentCaptor.getValue().getNickname()).isEqualTo("nick");
    }

    @Test
    @DisplayName("memberId가 존재하는지")
    void exists_by_memberId() {
        // given
        MemberId memberId = new MemberId(dto.getEmail(), MemberType.NORMAL);
        given(memberRepository.existsById(memberId)).willReturn(true);

        // when
        assertTrue(memberService.existsByMemberId(memberId));

        // then
        verify(memberRepository, times(1)).existsById(memberId);
    }

    @Test
    @DisplayName("이메일이 존재하는지")
    void exists_by_email() {
        // given
        given(memberRepository.existsByEmail(dto.getEmail())).willReturn(true);

        // when
        assertTrue(memberService.existsByEmail(dto.getEmail()));

        // then
        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    @DisplayName("닉네임이 존재하는지")
    void exists_by_nickname() {
        // given
        given(memberRepository.existsByNickname(dto.getNickname())).willReturn(true);

        // when
        assertTrue(memberService.existsByNickname(dto.getNickname()));

        // then
        verify(memberRepository, times(1)).existsByNickname(dto.getNickname());
    }
}
