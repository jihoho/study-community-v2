package com.project.pagu.modules.member.controller;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.common.manager.SignUpManager;
import com.project.pagu.common.validation.SignUpValidation;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/login")
    public String login(HttpServletRequest request, Principal principal) {
        //todo: 로그인 이슈
//        if (principal != null) {
//            return "redirect:/error";
//        }
        String referrer = request.getHeader("Referer");
        if(!referrer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referrer);
        }

        return "login";
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

        memberSaveRequestDto.createEmailAuthKey();
        signUpManager.sendAuthMessage(memberSaveRequestDto.getEmail(), memberSaveRequestDto.getAuthKey());
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
        memberService.login(memberSaveRequestDto.toEntity());
        sessionStatus.isComplete();
        return "redirect:/sign-up-success";
    }

    @GetMapping("/sign-up-success")
    public String signUpSuccess() {
        return "sign-up-success";
    }

    @GetMapping("/members/password-find")
    public String getPasswordFindForm(Model model) {
        model.addAttribute(new MemberSaveRequestDto());
        return "/members/email-check";
    }

    @PostMapping("/members/email-check")
    public String sendEmailAuthKey(HttpSession session, MemberSaveRequestDto memberSaveRequestDto) {
        String email = memberSaveRequestDto.getEmail();
        if (memberService.existsById(MemberId.of(email, MemberType.NORMAL))) {
            memberSaveRequestDto.createEmailAuthKey();
            signUpManager.sendAuthMessage(memberSaveRequestDto.getEmail(), memberSaveRequestDto.getAuthKey());
            session.setAttribute("memberSaveRequestDto", memberSaveRequestDto);
            return "redirect:/members/email-form";
        }

        return "redirect:/members/email-form";
    }

    /**
     * 세션에 담겨있는 memberSaveRequestDto 가져온다
     */
    @GetMapping("/members/email-form")
    public String getEmailAuthKeyForm(Model model, MemberSaveRequestDto memberSaveRequestDto) {
        model.addAttribute(memberSaveRequestDto);
        return "/members/email-check-password";
    }

    @PostMapping("/members/email-check-password")
    public String sendNewPassword(MemberSaveRequestDto memberSaveRequestDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(memberSaveRequestDto.getAuthKey(),
                memberSaveRequestDto.getAuthKeyInput(), result)) {
            return "/members/email-check-password";
        }

        String newPassword = signUpManager.sendNewPassword(memberSaveRequestDto.getEmail());
        memberService.changePassword(MemberId.of(memberSaveRequestDto.getEmail(), MemberType.NORMAL),
                        newPassword);

        sessionStatus.isComplete();
        return "redirect:/members/password-success";
    }

    @GetMapping("/members/password-success")
    public String passwordSuccess() {
        return "members/password-success";
    }

}
