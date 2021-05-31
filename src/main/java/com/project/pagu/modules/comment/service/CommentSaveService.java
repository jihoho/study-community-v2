package com.project.pagu.modules.comment.service;

import com.project.pagu.modules.comment.model.CommentSaveDto;
import com.project.pagu.modules.member.domain.MemberId;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/04 Time: 1:42 오후
 */
public interface CommentSaveService {

    Long saveComment(MemberId memberId, CommentSaveDto commentSaveDto);

    void updateComment(MemberId memberId, CommentSaveDto commentSaveDto);

    void deleteComment(MemberId memberId,long commentId);

    void deleteCommentsOnBoard(long boardId);

}
