package com.project.pagu.modules.board.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.service.BoardService;
import com.project.pagu.modules.member.domain.Member;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 12:31 오후
 */

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String boards(
            @PageableDefault(sort = "modifiedDate", direction = Direction.DESC) final Pageable pageable,
            Model model) {

        Page<Board> boardList = boardService.getPagedBoardList(pageable);
        model.addAttribute("boardList", boardList);
        return "boards/board-list";

    }

    @PostMapping("/boards")
    public String createBoard(@CurrentMember Member member,
            @Valid BoardSaveRequestDto boardSaveRequestDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "boards/board-form";
        }

        boardService.saveBoardDto(member, boardSaveRequestDto);
        return "redirect:/";
    }

    @GetMapping("/boards/board-form")
    public String boardsFrom(Model model, BoardSaveRequestDto boardSaveRequestDto) {
        model.addAttribute(boardSaveRequestDto);
        return "boards/board-form";
    }

    @GetMapping("/boards/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getBoardDetailDto(id));
        return "boards/board-detail";
    }

    @GetMapping("/boards/{id}/form")
    public String getBoardForUpdate(@PathVariable Long id) {
        /**
         * todo : 로그인 상태, 게시물 수정 폼
         */
        return "boards/board-update";
    }
}
