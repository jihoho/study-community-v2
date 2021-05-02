package com.project.pagu.modules.comment.model;

import com.project.pagu.modules.comment.domain.Comment;
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

    private String content;

    public Comment toEnity() {
        return Comment.builder().content(content).build();
    }

}