package com.project.pagu.modules.board.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.project.pagu.modules.board.model.BoardSaveDto;
import com.project.pagu.modules.board.model.BoardScheduleDto;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.board.service.BoardSaveService;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.mockMember.WithMember;
import com.project.pagu.modules.member.repository.MemberRepository;
import com.project.pagu.modules.member.service.MemberSaveService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 1:54 오후
 */

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardSaveService boardSaveService;

    @Autowired
    private MemberSaveService memberSaveService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("게시물 전체 페이지로 이동한다.")
    @Test
    void move_main_page() throws Exception {
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-list"))
                .andExpect(content().string(containsString("스터디 등록하기")))
                .andExpect(content().string(containsString("PAGU")))
                .andExpect(content().string(containsString("no")))
                .andExpect(content().string(containsString("title")))
                .andExpect(content().string(containsString("location")))
                .andExpect(content().string(containsString("writer")))
                .andExpect(content().string(containsString("reg date")))
                .andExpect(content().string(containsString("tag")))
                .andExpect(content().string(containsString("status")))
                .andDo(print());
    }

    @DisplayName("게시물 등록 페이지로 이동한다.")
    @Test
    @WithMember
    void boards_form() throws Exception {
        mockMvc.perform(get("/boards/board-form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boardSaveDto"))
                .andExpect(view().name("boards/board-form"))
                .andExpect(content().string(containsString("스터디 모집 공고 등록")))
                .andExpect(content().string(containsString("제목")))
                .andExpect(content().string(containsString("주제")))
                .andExpect(content().string(containsString("목표")))
                .andExpect(content().string(containsString("장소")))
                .andExpect(content().string(containsString("시간")))
                .andExpect(content().string(containsString("모집 기간")))
                .andExpect(content().string(containsString("스터디 기간")))
                .andDo(print());
    }

    @DisplayName("게시물 등록 페이지 접근 시 USER 꿘한이 아닐 꼉우 login 페이지로 리다이렉트")
    @Test
    void unauthorize_boards_form() throws Exception {
        mockMvc.perform(get("/boards/board-form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andDo(print());
    }


    @DisplayName("게시물 상세 페이지로 이동한다.")
    @WithMember
    @Test
    void get_board() throws Exception {
        //given
        Member member = memberRepository.save(givenMember(Role.USER));
        BoardSaveDto boardDto = givenBoardDto();
        Long id = boardSaveService.saveBoardDto(member.getMemberId(), boardDto);

        mockMvc.perform(get("/boards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-detail"))
                .andExpect(content().string(containsString(boardDto.getTitle())))
                .andExpect(content().string(containsString(boardDto.getSubjects())))
                .andExpect(content().string(containsString(boardDto.getTechStacks())))
                .andExpect(content().string(containsString(boardDto.getGoal())))
                .andExpect(content().string(containsString(boardDto.getPlace())))
                .andExpect(content().string(containsString(boardDto.getRecruitmentStartAt().format(
                        DateTimeFormatter.ISO_DATE))))
                .andExpect(content().string(containsString(boardDto.getRecruitmentEndAt().format(
                        DateTimeFormatter.ISO_DATE))))
                .andExpect(content().string(containsString(boardDto.getTermsStartAt().format(
                        DateTimeFormatter.ISO_DATE))))
                .andExpect(content().string(containsString(boardDto.getTermsEndAt().format(
                        DateTimeFormatter.ISO_DATE))))
                .andExpect(content().string(containsString(boardDto.getEtc())))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정페이지로 이동한다.")
    void get_board_update_form() throws Exception {
        Member member = memberRepository.save(givenMember(Role.USER));
        Long boardId= boardSaveService.saveBoardDto(member.getMemberId(),givenBoardDto());
        mockMvc.perform(get("/boards/{id}/update", boardId))
                .andExpect(status().isOk())
                .andExpect(view().name("boards/board-update"))
                .andExpect(content().string(containsString("스터디 모집 공고 수정")))
                .andExpect(content().string(containsString("제목")))
                .andExpect(content().string(containsString("주제")))
                .andExpect(content().string(containsString("목표")))
                .andExpect(content().string(containsString("장소")))
                .andExpect(content().string(containsString("시간")))
                .andExpect(content().string(containsString("모집 상태")))
                .andExpect(content().string(containsString("스터디 기간")))
                .andDo(print());
    }

    public Member givenMember(Role role) {
        return Member.builder()
                .email("tester@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .password(passwordEncoder.encode("abcde1234!"))
                .role(role)
                .imageFilename(null)
                .career("취준생")
                .postion("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }

    private BoardSaveDto givenBoardDto() {
        BoardScheduleDto scheduleDto = new BoardScheduleDto();
        scheduleDto.setDayKey(0);
        scheduleDto.setStartTime(LocalTime.of(10, 30));
        scheduleDto.setEndTime(LocalTime.of(13, 30));

        BoardSaveDto dto = new BoardSaveDto();
        dto.setTitle("스프링 스터디 모집합니다.");
        dto.setSubjects("Backend");
        dto.setTechStacks("Java");
        dto.setGoal("스프링링의 기본 원리를 이해한다.");
        dto.setPlace("강남역");
        dto.setBoardSchedules(List.of(scheduleDto));
        dto.setRecruitmentStartAt(LocalDate.now());
        dto.setRecruitmentEndAt(LocalDate.now().plusDays(30));
        dto.setTermsStartAt(LocalDate.now().plusDays(31));
        dto.setTermsEndAt(LocalDate.now().plusDays(60));
        dto.setEtc("기타 사항");
        return dto;
    }

}