package com.project.pagu.modules.member.controller;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.common.manager.SignUpManager;
import com.project.pagu.common.validation.SignUpValidation;

import com.project.pagu.modules.member.service.MemberViewService;
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

    private final MemberSaveService memberSaveService;
    private final MemberViewService memberViewService;
    private final SignUpManager signUpManager;
    private final SignUpValidation signUpValidation;

    @InitBinder("memberSaveRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpValidation);
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Principal principal) {
        //todo: 로그인 이슈
        if (principal != null) {
            return "redirect:/error";
        }

        String referrer = request.getHeader("Referer");

        if (referrer != null && referrer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referrer);
        }

        return "login";
    }


    @GetMapping("sign-up")
    public String signUp(Model model, SignUpDto signUpDto) {
        model.addAttribute(signUpDto);
        return "sign-up";
    }

    @PostMapping("/sign-up/valid")
    public String validMemberSaveForm(@Valid SignUpDto signUpDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "sign-up";
        }

        signUpDto.createEmailAuthKey();
        signUpManager.sendAuthMessage(signUpDto.getEmail(),
                signUpDto.getAuthKey());
        signUpManager.encryptPassword(signUpDto);
        model.addAttribute(signUpDto);
        return "redirect:/email-check";
    }

    @GetMapping("email-check")
    public String emailCheck(Model model, SignUpDto signUpDto) {
        if (signUpDto.getEmail().isEmpty()) {
            return "error";
        }
        model.addAttribute(signUpDto);
        return "email-check";
    }

    @PostMapping("/sign-up/email-check")
    public String emailCheckAndSaveMember(SignUpDto signUpDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(signUpDto.getAuthKey(),
                signUpDto.getAuthKeyInput(), result)) {
            return "email-check";
        }

        memberSaveService.saveMember(signUpDto);
        memberViewService.login(signUpDto.toEntity());
        sessionStatus.isComplete();
        return "redirect:/sign-up-success";
    }

    @GetMapping("/sign-up-success")
    public String signUpSuccess() {
        return "sign-up-success";
    }

    @GetMapping("/members/password-find")
    public String getPasswordFindForm(Model model) {
        model.addAttribute(new SignUpDto());
        return "/members/email-check";
    }

    @PostMapping("/members/email-check")
    public String sendEmailAuthKey(HttpSession session, SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        if (memberViewService.existsById(MemberId.of(email, MemberType.NORMAL))) {
            signUpDto.createEmailAuthKey();
            signUpManager.sendAuthMessage(signUpDto.getEmail(),
                    signUpDto.getAuthKey());
            session.setAttribute("memberSaveRequestDto", signUpDto);
            return "redirect:/members/email-form";
        }

        return "redirect:/members/email-form";
    }

    /**
     * 세션에 담겨있는 memberSaveRequestDto 가져온다
     */
    @GetMapping("/members/email-form")
    public String getEmailAuthKeyForm(Model model, SignUpDto signUpDto) {
        model.addAttribute(signUpDto);
        return "/members/email-check-password";
    }

    @PostMapping("/members/email-check-password")
    public String sendNewPassword(SignUpDto signUpDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(signUpDto.getAuthKey(),
                signUpDto.getAuthKeyInput(), result)) {
            return "/members/email-check-password";
        }

        String newPassword = signUpManager.sendNewPassword(signUpDto.getEmail());
        memberSaveService
                .changePassword(MemberId.of(signUpDto.getEmail(), MemberType.NORMAL),
                        newPassword);

        sessionStatus.isComplete();
        return "redirect:/members/password-success";
    }

    @GetMapping("/members/password-success")
    public String passwordSuccess() {
        return "members/password-success";
    }

}
