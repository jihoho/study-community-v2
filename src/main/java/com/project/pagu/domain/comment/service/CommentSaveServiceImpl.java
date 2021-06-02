package com.project.pagu.domain.comment.service;

import com.project.pagu.global.error.exception.AccessDeniedException;
import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.board.repository.BoardRepository;
import com.project.pagu.domain.comment.domain.Comment;
import com.project.pagu.domain.comment.dto.CommentSaveDto;
import com.project.pagu.domain.comment.repository.CommentRepository;
import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberId;
import com.project.pagu.domain.member.repository.MemberRepository;
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
public class CommentSaveServiceImpl implements CommentSaveService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long saveComment(MemberId memberId, CommentSaveDto commentSaveDto) {

        // MemberNotFoundException, BoardNotFoundException 추후 추가
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);
        Board board = boardRepository.findById(commentSaveDto.getBoardId())
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = commentSaveDto.toEntity();
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
    public void updateComment(MemberId memberId, CommentSaveDto commentSaveDto) {
        Comment findComment = commentRepository.findById(commentSaveDto.getCommentId())
                .orElseThrow(IllegalAccessError::new);

        if (!findComment.getMember().getMemberId().equals(memberId)) {
            throw new AccessDeniedException("해당 댓글의 주인이 아닙니다.");
        }
        findComment.updateContent(commentSaveDto.getContent());
    }

    @Transactional
    public void deleteComment(MemberId memberId, long commentId) {
        Comment targetComment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        if(!targetComment.getMember().getMemberId().equals(memberId)){
            throw new AccessDeniedException("해당 댓글의 주인이 아닙니다.");
        }
        if (!targetComment.isRemove()) {
            targetComment.remove();
        }
    }

}
