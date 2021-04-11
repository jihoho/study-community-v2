package com.project.pagu.member.controller;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.MemberSaveRequestDto;
import com.project.pagu.member.service.MemberService;
import com.project.pagu.signup.SignUpManager;
import com.project.pagu.validation.SignUpValidation;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:06 오후
 */

@Controller
@RequiredArgsConstructor
@SessionAttributes("memberSaveRequestDto")
public class MemberController {

    private final MemberService memberService;
    private final SignUpManager signUpManager;
    private final SignUpValidation signUpValidation;

    @InitBinder("memberSaveRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpValidation);
    }

    @GetMapping("profile")
    public String profile(/* @AuthenticationPrincipal Member member */) {
        /**
         * 로그인 상태
         */
        return "profile";
    }

    @GetMapping("sign-up")
    public String signUp(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        model.addAttribute(memberSaveRequestDto);
        return "sign-up";
    }

    @PostMapping("/sign-up/valid")
    public String validMemberSaveForm(@Valid MemberSaveRequestDto memberSaveRequestDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "sign-up";
        }

        signUpManager.sendMessageToMemberDto(memberSaveRequestDto);
        signUpManager.encryptPassword(memberSaveRequestDto);
        model.addAttribute(memberSaveRequestDto);
        return "redirect:/email-check";
    }

    @GetMapping("email-check")
    public String emailCheck(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        if (memberSaveRequestDto.getEmail().isEmpty()) {
            return "error";
        }
        model.addAttribute(memberSaveRequestDto);
        return "email-check";
    }

    @PostMapping("/sign-up/email-check")
    public String emailCheckAndSaveMember(MemberSaveRequestDto memberSaveRequestDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(memberSaveRequestDto.getAuthKey(),
                    memberSaveRequestDto.getAuthKeyInput(), result)) {
            return "email-check";
        }

        memberService.saveMember(memberSaveRequestDto);
        sessionStatus.isComplete();
        return "redirect:/sign-up-success";
    }

    @GetMapping("/sign-up-success")
    public String signUpSuccess() {
        return "sign-up-success";
    }

}
