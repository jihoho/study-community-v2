package com.project.pagu.domain.board.controller;

import static org.springframework.data.domain.Sort.Direction.*;

import com.project.pagu.global.annotation.CurrentMember;
import com.project.pagu.global.util.FileManager;
import com.project.pagu.domain.board.dto.BoardSaveDto;
import com.project.pagu.domain.board.dto.WriterDto;
import com.project.pagu.domain.board.service.BoardSaveService;
import com.project.pagu.domain.board.service.BoardViewService;
import com.project.pagu.domain.member.domain.Member;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 12:31 오후
 */

@Controller
@RequiredArgsConstructor
public class BoardController {

    private static final String SORT = "modifiedDate";
    private static final String DEFAULT_TYPE = "TITLE";

    private final BoardSaveService boardSaveService;
    private final BoardViewService boardViewService;
    private final FileManager fileManager;

    /**
     * 전체 게시물을 페이징 처리하여 조회한다.
     */
    @GetMapping("/boards")
    public String boards(@PageableDefault(sort = SORT, direction = DESC) final Pageable pageable, Model model) {
        model.addAttribute("boardList", boardViewService.getPagedBoardList(pageable));
        model.addAttribute("searchType", DEFAULT_TYPE);
        return "boards/board-list";
    }

    /**
     * 게시물 등록 페이지로 이동한다.
     */
    @GetMapping("/boards/board-form")
    public String boardsFrom(Model model) {
        model.addAttribute(new BoardSaveDto());
        return "boards/board-form";
    }

    /**
     * 게시물 등록 데이터를 받아 게시물을 저장한다.
     */
    @PostMapping("/boards/board-form")
    public String createBoard(@CurrentMember Member member, @Valid BoardSaveDto boardSaveDto, BindingResult result) {

        if (result.hasErrors()) {
            return "boards/board-form";
        }

        boardSaveService.saveBoardDto(member.getMemberId(), boardSaveDto);
        return "redirect:/";
    }

    /**
     * 게시물 단건 조회한다.
     */
    @GetMapping("/boards/{id}")
    public String getBoard(@CurrentMember Member member, @PathVariable Long id, Model model) {
        model.addAttribute("board", boardViewService.getBoardDetailDto(id));

        if (member != null) {
            model.addAttribute("member", WriterDto.createWriterDto(member));
        }

        return "boards/board-detail";
    }

    @GetMapping("/boards/{id}/update")
    public String getBoardForUpdate(@PathVariable Long id, Model model) {
        model.addAttribute(boardViewService.getBoardSaveDto(id));
        return "boards/board-update";
    }

    @PostMapping("/boards/{id}/update")
    public String update(@CurrentMember Member member, @PathVariable Long id,
            @Valid BoardSaveDto dto, BindingResult result) {

        if (result.hasErrors()) {
            return "boards/board-update";
        }

        boardSaveService.update(member.getMemberId(), id, dto);

        return "redirect:/";
    }

    @GetMapping("/boards/search")
    public String search(@RequestParam String searchType,
            @RequestParam String keyword,
            @PageableDefault(sort = SORT, direction = DESC) final Pageable pageable,
            Model model) {

        model.addAttribute("boardList",
                boardViewService.getSearchBoards(searchType, keyword, pageable));
        model.addAttribute(searchType);
        model.addAttribute(keyword);
        return "boards/board-list";
    }

    @GetMapping("/boards/thumbnails/{id}/{filename}")
    public void boardImageThumbnails(@PathVariable String id, @PathVariable String filename,
            HttpServletResponse response) throws Exception {
        fileManager.boardThumbnails(response, filename, id);
    }
}