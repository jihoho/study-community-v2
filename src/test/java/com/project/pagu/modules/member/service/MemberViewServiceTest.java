package com.project.pagu.modules.member.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.domain.UserMember;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/03 Time: 3:25 오후
 */

@ExtendWith(MockitoExtension.class)
public class MemberViewServiceTest {

    @InjectMocks
    private MemberViewServiceImpl memberViewService;

    @Mock
    private MemberRepository memberRepository;

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
    @DisplayName("memberId가 존재하는지")
    void exists_by_memberId() {
        // given
        MemberId memberId = MemberId.of(dto.getEmail(), MemberType.NORMAL);
        given(memberRepository.existsById(memberId)).willReturn(true);

        // when
        assertTrue(memberViewService.existsById(memberId));

        // then
        verify(memberRepository, times(1)).existsById(memberId);
    }

    @Test
    @DisplayName("닉네임이 존재하는지")
    void exists_by_nickname() {
        // given
        given(memberRepository.existsByNickname(dto.getNickname())).willReturn(true);

        // when
        assertTrue(memberViewService.existsByNickname(dto.getNickname()));

        // then
        verify(memberRepository, times(1)).existsByNickname(dto.getNickname());
    }

    @Test
    @DisplayName("아이디로 회원 조회")
    void find_by_email() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.of(dto.toEntity()));

        // when
        Member member = memberViewService.findById(MemberId.of(dto.getEmail(), MemberType.NORMAL));

        // then
        verify(memberRepository, times(1)).findById(any());
        assertNotNull(member);
    }

    @Test
    @DisplayName("loadUserByUsername 테스트")
    void load_user_by_username(){
        // given
        Member member = dto.toEntity();
        Optional<Member> optionalMember=Optional.of(member);
        given(memberRepository.findById(any())).willReturn(optionalMember);

        // when
        assertEquals(memberViewService.loadUserByUsername(member.getEmail()),new UserMember(member));

        // then
        verify(memberRepository, times(1)).findById(any());
    }


    @Test
    @DisplayName("Member엔티티 profileDto 변환 테스트")
    void  convert_member_to_profile_dto() throws Exception {
        // given
        Member targetMember = Member.builder()
                .email("email@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("nick")
                .password("1234")
                .imageFilename("filename.png")
                .link("https://link.com")
                .info("안녕하세요")
                .career("취준생")
                .postion("웹 백엔드")
                .role(Role.GUEST)
                .build();

        ProfileDto expectedDto = ProfileDto.builder()
                .email("email@email.com")
                .memberType(MemberType.NORMAL.getKey())
                .nickname("nick")
                .imageFilename("filename.png")
                .link("https://link.com")
                .info("안녕하세요")
                .career("취준생")
                .position("웹 백엔드")
                .build();

        given(memberRepository.findByNickname(any())).willReturn(Optional.of(targetMember));

        // when
        ProfileDto resultDto = memberViewService.convertToProfileViewDtoBy("nick");

        // then
        assertAll(
                ()->assertEquals(expectedDto.getEmail(),resultDto.getEmail()),
                ()->assertEquals(expectedDto.getMemberType(),resultDto.getMemberType()),
                ()->assertEquals(expectedDto.getNickname(),resultDto.getNickname()),
                ()->assertEquals(expectedDto.getImageFilename(),resultDto.getImageFilename()),
                ()->assertEquals(expectedDto.getLink(),resultDto.getLink()),
                ()->assertEquals(expectedDto.getCareer(),resultDto.getCareer()),
                ()->assertEquals(expectedDto.getPosition(),resultDto.getPosition())
        );
    }


}
