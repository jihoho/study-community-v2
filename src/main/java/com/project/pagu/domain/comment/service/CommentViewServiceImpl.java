package com.project.pagu.domain.comment.service;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.board.repository.BoardRepository;
import com.project.pagu.domain.comment.domain.Comment;
import com.project.pagu.domain.comment.dto.CommentViewDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/04 Time: 1:50 오후
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentViewServiceImpl implements CommentViewService {

    private final BoardRepository boardRepository;

    @Override
    //todo : 이 로직은 boardService에 있는게 더 맞지않은지
    public List<CommentViewDto> findCommentsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        List<Comment> comments = board.getComments();
        return convertNestedStructure(comments);
    }

    private List<CommentViewDto> convertNestedStructure(List<Comment> comments) {
        List<CommentViewDto> result = new ArrayList<>();
        Map<Long, CommentViewDto> map = new HashMap<>();
        comments.stream().forEach(
                c -> {
                    CommentViewDto dto = CommentViewDto.createCommentResponseDto(c);
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
}
