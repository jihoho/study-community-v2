package com.project.pagu.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.model.MemberSaveRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오후 4:14
 */
@ExtendWith(MockitoExtension.class)
class MembersServiceTest {

    @Mock
    MembersService membersService;
    private MemberSaveRequestDto dto =
            MemberSaveRequestDto.builder()
                    .email("123@naver.com")
                    .nickname("nick")
                    .password("abcde1234!")
                    .passwordCheck("abcde1234!")
                    .build();
    
    @Test
    @DisplayName("회원 저장 테스트")
    void 회원_저장() throws Exception{
        // given
        when(membersService.save(dto)).thenReturn(new MemberId(dto.getEmail(), MemberType.NORMAL));
        when(membersService.existsById(any()));
        // when
      
        // then
    }
    
}