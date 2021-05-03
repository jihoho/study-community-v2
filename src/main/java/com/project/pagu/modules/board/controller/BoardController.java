package com.project.pagu.modules.board.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.board.model.BoardSaveRequestDto;
import com.project.pagu.modules.board.model.WriterDto;
import com.project.pagu.modules.board.service.BoardService;
import com.project.pagu.modules.comment.model.CommentSaveDto;
import com.project.pagu.modules.comment.model.CommentUpdateDto;
import com.project.pagu.modules.member.domain.Member;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final FileManager fileManager;

    @GetMapping("/boards")
    public String boards(
            @PageableDefault(sort = "modifiedDate", direction = Direction.DESC) final Pageable pageable,
            Model model) {

        model.addAttribute("boardList", boardService.getPagedBoardList(pageable));
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
    public String getBoard(@CurrentMember Member member, @PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getBoardDetailDto(id));

        if (member != null) {
            model.addAttribute("member", WriterDto.createWriterDto(member));
        }

        return "boards/board-detail";
    }

    @GetMapping("/boards/{id}/form")
    public String getBoardForUpdate(@PathVariable Long id) {
        /**
         * todo : 로그인 상태, 게시물 수정 폼
         */
        return "boards/board-update";
    }

    @GetMapping("/boardImageThumbnails/{id}/{filename}")
    public void boardImageThumbnails(@PathVariable String id, @PathVariable String filename,
            HttpServletResponse response) throws Exception {
        fileManager.boardThumbnails(response, filename, id);
    }
}
