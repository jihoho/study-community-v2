package com.project.pagu.member.controller;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.email.service.EmailService;
import com.project.pagu.member.service.MemberService;
import com.project.pagu.member.model.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:06 오후
 */

@Controller
@RequiredArgsConstructor
@SessionAttributes("memberSaveRequestDto")
public class MembersController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("profile")
    public String profile(/* @AuthenticationPrincipal Member member */) {
        /**
         * 로그인 상태
         */
        return "profile";
    }

    @GetMapping("sign-up")
    public String signUp(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        model.addAttribute("memberSaveRequestDto", memberSaveRequestDto);
        return "sign-up";
    }

    @GetMapping("email-check")
    public String emailCheck(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        model.addAttribute("memberSaveRequestDto", memberSaveRequestDto);
        return "email-check";
    }

    @GetMapping("sign-up-success")
    public String signUpSuccess(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        model.addAttribute(memberSaveRequestDto);
        return "sign-up-success";
    }

    @PostMapping("/members/valid")
    public String validMemberSaveForm(@Valid MemberSaveRequestDto memberSaveRequestDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "sign-up";
        }
        emailService.sendMessageToMemberDto(memberSaveRequestDto);
        model.addAttribute(memberSaveRequestDto);
        return "redirect:/email-check";
    }

    @PostMapping("/members/email-check")
    public String emailCheckAndSaveMember(@Valid MemberSaveRequestDto memberSaveRequestDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "email-check";
        }

        memberService.encryptPassword(memberSaveRequestDto);
        MemberId memberId = memberService.save(memberSaveRequestDto);
        model.addAttribute(memberSaveRequestDto);
        return "redirect:/sign-up-success";
    }

}
