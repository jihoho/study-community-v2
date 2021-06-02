package com.project.pagu.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-03 Time: 오후 3:53
 */
@Getter
@Setter
public class CommentUpdateDto {
    private Long boardId;
    private Long commentId;
    private String content;
}
