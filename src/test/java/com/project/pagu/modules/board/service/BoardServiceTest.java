package com.project.pagu.modules.board.service;

import static java.time.Month.APRIL;
import static java.time.Month.MAY;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.modules.tag.Subject;
import com.project.pagu.modules.tag.SubjectService;
import com.project.pagu.modules.teckstack.TechStack;
import com.project.pagu.modules.teckstack.TechStackService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.BDDAssertions;
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
 * Date: 2021/04/27 Time: 4:15 오후
 */

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private TechStackService techStackService;

    @Captor
    private ArgumentCaptor<Board> argumentCaptor;

    @Test
    @DisplayName("게시물을 등록한다.")
    void save_board() {
        Member member = givenMember();
        given(memberService.findById(any())).willReturn(member);
        given(subjectService.getOrSave(any())).willReturn(Subject.of("Spring"));
        given(techStackService.getOrSave(any())).willReturn(TechStack.of("Spring"));
        given(boardRepository.save(any())).willReturn(givenBoardDto().toEntity());

        boardService.saveBoardDto(member, givenBoardDto());

        verify(boardRepository, times(1)).save(argumentCaptor.capture());

        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("스프링 스터디 모집합니다.");
        then(argumentCaptor.getValue().getBoardSubjects().size()).isEqualTo(1);
        then(argumentCaptor.getValue().getBoardTechStacks().size()).isEqualTo(1);
        then(argumentCaptor.getValue().getGoal()).isEqualTo("스프링링의 기본 원리를 이해한다.");
        then(argumentCaptor.getValue().getBoardSchedules().size()).isEqualTo(1);
        then(argumentCaptor.getValue().getRecruitmentStartAt().getMonth()).isEqualTo(APRIL);
        then(argumentCaptor.getValue().getRecruitmentEndAt().getMonth()).isEqualTo(MAY);
        then(argumentCaptor.getValue().getTermsStartAt().getMonth()).isEqualTo(MAY);
        then(argumentCaptor.getValue().getTermsEndAt().getMonth()).isEqualTo(MAY);
        then(argumentCaptor.getValue().getEtc()).isEqualTo("기타 사항");
    }

    public Member givenMember() {
        return Member.builder()
                .email("test@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .role(Role.GUEST)
                .imageUrl(null)
                .career("취준생")
                .postion("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }

    private BoardSaveRequestDto givenBoardDto() {
        BoardScheduleDto scheduleDto = new BoardScheduleDto();
        scheduleDto.setDayKey(0);
        scheduleDto.setStartTime(LocalTime.of(10, 30));
        scheduleDto.setEndTime(LocalTime.of(13, 30));

        BoardSaveRequestDto dto = new BoardSaveRequestDto();
        dto.setTitle("스프링 스터디 모집합니다.");
        dto.setSubjects("Backend");
        dto.setTechStacks("Java");
        dto.setGoal("스프링링의 기본 원리를 이해한다.");
        dto.setBoardSchedules(List.of(scheduleDto));
        dto.setRecruitmentStartAt(LocalDate.of(2021, 04, 27));
        dto.setRecruitmentEndAt(LocalDate.of(2021, 05, 4));
        dto.setTermsStartAt(LocalDate.of(2021, 05, 12));
        dto.setTermsEndAt(LocalDate.of(2021, 05, 24));
        dto.setEtc("기타 사항");
        return dto;
    }

}