package com.project.pagu.domain.comment.service;

import com.project.pagu.domain.comment.dto.CommentSaveDto;
import com.project.pagu.domain.model.MemberId;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/04 Time: 1:42 오후
 */
public interface CommentSaveService {

    Long saveComment(MemberId memberId, CommentSaveDto commentSaveDto);

    void updateComment(MemberId memberId, CommentSaveDto commentSaveDto);

    void deleteComment(MemberId memberId,long commentId);

}
