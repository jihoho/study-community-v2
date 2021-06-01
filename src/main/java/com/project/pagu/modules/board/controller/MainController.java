package com.project.pagu.modules.board.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.modules.board.service.BoardViewService;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.model.OauthSaveDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.common.validation.OauthSignUpValidation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

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
