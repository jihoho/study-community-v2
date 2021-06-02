package com.project.pagu.domain.board.repository;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.model.StudyStatus;
import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberType;
import com.project.pagu.domain.model.Role;
import com.project.pagu.domain.member.repository.MemberRepository;
import com.project.pagu.domain.model.BoardSubject;
import com.project.pagu.domain.subject.domain.Subject;
import com.project.pagu.domain.model.BoardTechStack;
import com.project.pagu.domain.model.TechStack;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-28 Time: 오후 6:48
 */
@DataJpaTest
@DisplayName("BoardRepository 테스트")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물 등록 및 조회")
    void save_board() throws Exception {

        // given
        Member savedMember = memberRepository.save(givenMember());
        Board board = givenBoard();
        board.setMember(savedMember);
        board.addSubject(giveBoardSubject("Web"));
        board.addTechStack(givenBoardTeckStack("Java"));

        // when
        Board savedBoard = boardRepository.save(board);

        // then
        then(savedBoard).isEqualTo(boardRepository.findById(savedBoard.getId()).get());
        then(savedBoard).isEqualTo(
                memberRepository.findById(savedMember.getMemberId()).get().getBoards().get(0));

    }


    @Test
    @DisplayName("게시물 페이징 요청")
    @Transactional
    void get_paged_board() throws Exception {

        // given
        int totalSize = 13;
        Member savedMember = memberRepository.save(givenMember());
        testSaveBoard(totalSize, savedMember);
        Pageable pageable = givenPageable(0, 10, Sort.by("modifiedDate").descending());

        // when
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // then
        then(boardPage.getSort()).isEqualTo(Sort.by("modifiedDate").descending());
        then(boardPage.getTotalElements()).isEqualTo(totalSize);
        then(boardPage.getPageable().getPageNumber()).isEqualTo(0);
        then(boardPage.getContent().size()).isEqualTo(10);

    }

    private void testSaveBoard(int totalSize, Member member) {
        for (int i = 0; i < totalSize; i++) {
            Board board = givenBoard();
            board.setMember(member);
        }
    }

    private Board givenBoard() {
        return Board.builder()
                .title("제목")
                .goal("목표")
                .place("강남역 ")
                .recruitmentStartAt(LocalDate.now())
                .recruitmentEndAt(LocalDate.now().plusDays(30))
                .termsStartAt(LocalDate.now().plusDays(30))
                .termsEndAt(LocalDate.now().plusDays(30))
                .status(StudyStatus.READY)
                .etc("기타 사항").build();
    }

    private BoardSubject giveBoardSubject(String subject) {
        return BoardSubject.of(givenSubject(subject));
    }

    private Subject givenSubject(String subject) {
        return Subject.of(subject);
    }

    private BoardTechStack givenBoardTeckStack(String techStack) {
        return BoardTechStack.of(givenTeckStack(techStack));
    }

    private TechStack givenTeckStack(String techStack) {
        return TechStack.of(techStack);
    }


    private Member givenMember() {
        return Member.builder()
                .email("test@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .role(Role.GUEST)
                .imageFilename(null)
                .career("취준생")
                .position("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
    }


    private Pageable givenPageable(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}