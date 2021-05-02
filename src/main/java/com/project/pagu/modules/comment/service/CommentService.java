package com.project.pagu.modules.comment.service;

import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.repository.BoardRepository;
import com.project.pagu.modules.comment.domain.Comment;
import com.project.pagu.modules.comment.model.CommentResponseDto;
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

    public List<CommentResponseDto> findCommentsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        List<Comment> comments = board.getComments();
        return convertNestedStructure(comments);
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

}
