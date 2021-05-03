package com.project.pagu.modules.comment.service;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.comment.domain.Comment;
import com.project.pagu.modules.comment.model.CommentResponseDto;
import com.project.pagu.modules.comment.model.CommentSaveDto;
import com.project.pagu.modules.comment.model.CommentUpdateDto;
import com.project.pagu.modules.comment.repository.CommentRepository;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 3:03
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public List<CommentResponseDto> findCommentsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        List<Comment> comments = board.getComments();
        return convertNestedStructure(comments);
    }

    @Transactional
    public Long saveComment(Member writer, CommentSaveDto commentSaveDto) {

        // MemberNotFoundException, BoardNotFoundException 추후 추가
        Member findMember = memberRepository.findById(writer.getMemberId())
                .orElseThrow(IllegalArgumentException::new);
        Board board = boardRepository.findById(commentSaveDto.getBoardId())
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = commentSaveDto.toEnity();
        comment.registerMember(findMember);
        comment.registerBoard(board);
        if (commentSaveDto.getSuperCommentId() != null) {
            commentRepository.findById(commentSaveDto.getSuperCommentId()).ifPresent(
                    superComment -> comment.registerSuperComment(superComment)
            );
        }
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void updateComment(Member writer, CommentUpdateDto commentUpdateDto) throws Exception {
        Comment findComment = findById(commentUpdateDto.getCommentId());
        if (findComment == null || !findComment.getMember().getMemberId()
                .equals(writer.getMemberId())) {
            throw new Exception("해당 댓글의 주인이 아닙니다.");
        }
        findComment.updateContent(commentUpdateDto.getContent());
    }

    @Transactional
    public void deleteComment(long commentId) {
        Comment targetComment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        if (targetComment.isRemove() == false) {
            targetComment.remove();
        }
    }

    private List<CommentResponseDto> convertNestedStructure(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();
        comments.stream().forEach(
                c -> {
                    CommentResponseDto dto = CommentResponseDto.createCommentResponseDto(c);
                    map.put(dto.getCommentId(), dto);
                    if (c.getSuperComment() != null) {
                        map.get(c.getSuperComment().getId()).getSubComment().add(dto);
                    } else {
                        result.add(dto);
                    }
                }
        );
        return result;
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
