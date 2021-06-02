package com.project.pagu.domain.comment.controller;

import com.project.pagu.global.annotation.CurrentMember;
import com.project.pagu.domain.comment.dto.CommentSaveDto;
import com.project.pagu.domain.comment.service.CommentSaveService;
import com.project.pagu.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-30 Time: 오후 5:52
 */

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentSaveService commentSaveService;

    /**
     * 비동기 처리 수정 예정
     */
    @PostMapping("/comments")
    public String createComment(@CurrentMember Member writer, CommentSaveDto commentSaveDto) {
        commentSaveService.saveComment(writer.getMemberId(), commentSaveDto);

        return "redirect:/boards/" + commentSaveDto.getBoardId();
    }

    @PostMapping("/comments/update")
    public String updateComment(@CurrentMember Member writer, CommentSaveDto commentSaveDto) {
        commentSaveService.updateComment(writer.getMemberId(), commentSaveDto);
        return "redirect:/boards/" + commentSaveDto.getBoardId();
    }

    @DeleteMapping("/comments/{id}")
    @ResponseBody
    public ResponseEntity deleteComment(@CurrentMember Member member, @PathVariable Long id) {
        commentSaveService.deleteComment(member.getMemberId(),id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 댓글 수정 기능 구현 예정
     */
}
