package com.project.pagu.modules.comment.service;

import static org.junit.jupiter.api.Assertions.*;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.domain.StudyStatus;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.comment.domain.Comment;
import com.project.pagu.modules.comment.model.CommentViewDto;
import com.project.pagu.modules.comment.model.CommentSaveDto;
import com.project.pagu.modules.comment.repository.CommentRepository;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import com.project.pagu.modules.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 6:11
 */

@SpringBootTest
@DisplayName("Comment 서비스 테스트")
class CommentSaveServiceImplTest {

    /**
     * Repository 모킹으로 수정
     */

    @Autowired
    CommentSaveService commentSaveService;
    @Autowired
    CommentViewService commentViewService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    void beforeEach() {
        Member member = givenMember();
        memberRepository.save(member);
    }



    @Test
    @DisplayName("계층형 대댓글 구조 테스트")
    @Transactional
    void get_hierarchical_comment_list() throws Exception {
        // given
        Member member = memberRepository.findByNickname("tester")
                .orElseThrow(IllegalArgumentException::new);
        Board savedBoard = boardRepository.save(givenBoard());
        savedBoard.setMember(member);

        commetTestSave(member.getMemberId(), savedBoard);

        // when
        Board findBoard = boardRepository.findById(savedBoard.getId())
                .orElseThrow(IllegalArgumentException::new);
        List<CommentViewDto> result = commentViewService.findCommentsByBoardId(findBoard.getId());

        for (CommentViewDto dto : result) {
            System.out.println(dto);
        }
        // then

        assertEquals(result.size(), 2); // 최상위 댓글 수
        assertEquals(result.get(0).getContent(), "content1"); // comment1
        assertEquals(result.get(1).getContent(), "content8"); // comment8
        assertEquals(result.get(0).getSubComment().size(), 2); // comment1의 자식 수
        // comment1의 첫 자식 comment2
        assertEquals(result.get(0).getSubComment().get(0).getContent(), "content2");
        // comment1의 두번째 자식 comment3
        assertEquals(result.get(0).getSubComment().get(1).getContent(), "content3");
        // comment2의 첫번째 자식 comment4
        assertEquals(result.get(0).getSubComment().get(0).getSubComment().get(0).getContent(),
                "content4");
        // comment2의 두번째 자식 comment5
        assertEquals(result.get(0).getSubComment().get(0).getSubComment().get(1).getContent(),
                "content5");
        // comment3의 첫번째 자식 comment7
        assertEquals(result.get(0).getSubComment().get(1).getSubComment().get(0).getContent(),
                "content7");
        // comment4의 첫번째 자식 comment6
        assertEquals(
                result.get(0).getSubComment().get(0).getSubComment().get(0).getSubComment().get(0)
                        .getContent(), "content6");
        // comment8 자식 없음
        assertEquals(result.get(1).getSubComment().size(), 0);
    }

    /**
     * 댓글 구조
     * 1
     * **2
     * ****4
     * ******6
     * ****5
     * **3
     * ****7
     * 8
     */
    private void commetTestSave(MemberId memberId, Board board) {
        Long commentId1 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, null, "content1"));

        Long commentId2 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId1, "content2"));

        Long commentId3 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId1, "content3"));

        Long commentId4 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId2, "content4"));

        Long commentId5 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId2, "content5"));

        Long commentId6 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId4, "content6"));

        Long commentId7 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, commentId3, "content7"));

        Long commentId8 = commentSaveService
                .saveComment(memberId, givenCommentSaveDto(board, null, "content8"));
    }

    private CommentSaveDto givenCommentSaveDto(Board board, Long superId,
            String content) {
        return CommentSaveDto.builder()
                .boardId(board.getId())
                .content(content)
                .superCommentId(superId)
                .build();
    }


    @Test
    @DisplayName("댓글 삭제")
    @Transactional
    void delete_comment_test() throws Exception {
        // given
        Member writer = memberRepository.findByNickname("tester")
                .orElseThrow(IllegalArgumentException::new);
        Board savedBoard = boardRepository.save(givenBoard());
        savedBoard.setMember(writer);
        Long commentId1 = commentSaveService
                .saveComment(writer.getMemberId(), givenCommentSaveDto(savedBoard, null, "content1"));
        Long commentId2 = commentSaveService
                .saveComment(writer.getMemberId(), givenCommentSaveDto(savedBoard, commentId1, "content2"));

        // when
        commentSaveService.deleteComment(writer.getMemberId(),commentId2);

        // then
        Comment comment2 = commentRepository.findById(commentId2).orElse(null);
        then(comment2.isRemove()).isTrue();
        then(comment2.getContent()).isEqualTo("삭제된 댓글 입니다.");
        then(comment2.getBackupContent()).isEqualTo("content2");
    }


    private Member givenMember() {
        return Member.builder()
                .email("test@email.com")
                .memberType(MemberType.NORMAL)
                .nickname("tester")
                .role(Role.GUEST)
                .oauthImageUrl(null)
                .career("취준생")
                .position("백엔드")
                .link("test@gi.com")
                .info("안녕하세요")
                .build();
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

}