package com.project.pagu.modules.comment.service;

import com.project.pagu.modules.comment.model.CommentSaveDto;
import com.project.pagu.modules.member.domain.Member;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/04 Time: 1:42 오후
 */
public interface CommentSaveService {

    //todo : memberId로
    Long saveComment(Member writer, CommentSaveDto commentSaveDto);

    //todo : memberId로
    void updateComment(Member writer, CommentSaveDto commentSaveDto);

    void deleteComment(long commentId);

}
