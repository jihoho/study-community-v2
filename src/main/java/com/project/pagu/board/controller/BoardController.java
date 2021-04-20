package com.project.pagu.board.controller;

import com.project.pagu.annotation.CurrentMember;
import com.project.pagu.board.model.BoardSaveRequestDto;
import com.project.pagu.board.service.BoardService;
import com.project.pagu.member.domain.Member;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 12:31 오후
 */

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String boards() {
        /**
         * todo : 로그인 상태, 게시물 페이징
         */
        return "boards/board-list";
    }

    @PostMapping()
    public String createBoard(@CurrentMember Member member,
            @Valid BoardSaveRequestDto boardSaveRequestDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "boards/board-form";
        }

        boardService.saveBoardDto(member, boardSaveRequestDto);
        return "redirect:/";
    }

    @GetMapping("/form")
    public String boardsFrom(Model model, BoardSaveRequestDto boardSaveRequestDto) {
        model.addAttribute(boardSaveRequestDto);
        return "boards/board-form";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id) {
        /**
         * todo : 로그인 상태, 게시물 단건 조회
         */
        return "boards/board-detail";
    }

    @GetMapping("/{id}/form")
    public String getBoardForUpdate(@PathVariable Long id) {
        /**
         * todo : 로그인 상태, 게시물 수정 폼
         */
        return "boards/board-update";
    }
}
