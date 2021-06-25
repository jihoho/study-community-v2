package com.project.pagu.domain.comment.dto;

import com.project.pagu.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 3:17
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveDto {

    private Long boardId;

    private Long superCommentId;

    private Long commentId;

    private String content;

    public Comment toEntity() {
        return Comment.builder().content(content).build();
    }

}