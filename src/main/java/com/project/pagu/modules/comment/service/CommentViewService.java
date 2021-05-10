package com.project.pagu.modules.comment.service;

import com.project.pagu.modules.comment.model.CommentViewDto;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/04 Time: 1:43 오후
 */
public interface CommentViewService {

    List<CommentViewDto> findCommentsByBoardId(Long boardId);

}
