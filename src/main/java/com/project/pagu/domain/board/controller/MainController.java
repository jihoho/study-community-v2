package com.project.pagu.domain.board.controller;

import com.project.pagu.domain.board.service.BoardViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/03/31 Time: 5:44 오후
 */

@RequiredArgsConstructor
@Controller
public class MainController {
    private final BoardViewService boardViewService;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("boardList", boardViewService.getLatestBoard(10));
        return "main";
    }

    @GetMapping("/authority")
    public String authority() {
        return "authority";
    }
}
