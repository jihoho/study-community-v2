package com.project.pagu.modules.member.controller;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.PasswordSaveDto;
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
@SessionAttributes("signUpDto")
public class MemberController {

    private final MemberSaveService memberSaveService;
    private final MemberViewService memberViewService;
    private final SignUpManager signUpManager;
    private final SignUpValidation signUpValidation;

    @InitBinder("signUpDto")
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

    /**
     * 회원가입 페이지로 이동한다.
     */
    @GetMapping("/members/sign-up")
    public String signUp(Model model, SignUpDto signUpDto) {
        model.addAttribute(signUpDto);
        return "members/sign-up";
    }

    /**
     * 회원 가입 폼을 전송 받아 검증 후 이메일 확인 페이지로 이동한다.
     */
    @PostMapping("/members/sign-up")
    public String validMemberSaveForm(@Valid SignUpDto signUpDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "members/sign-up";
        }

        signUpDto.createEmailAuthKey();
        signUpManager.sendAuthMessage(signUpDto.getEmail(), signUpDto.getAuthKey());
        signUpManager.encryptPassword(signUpDto);
        model.addAttribute(signUpDto);
        return "redirect:/members/sign-up/email-check";
    }

    /**
     * 세션에 담겨있는 회원 가입 폼으로 이메일 인증 페이지로 이동한다.
     * 세션에 이메일이 없을 경우 에러페이지로 이동한다.
     */
    @GetMapping("/members/sign-up/email-check")
    public String emailCheck(Model model, SignUpDto signUpDto) {
        if (signUpDto.getEmail().isBlank() || signUpDto == null) {
            return "error";
        }
        model.addAttribute(signUpDto);
        return "members/sign-up/email-check";
    }

    /**
     * 이메일이 인증되면 회원데이터를 저장하고 로그인한다.
     * 실패시 다시 이메일 인증 페이지로 이동한다.
     */
    @PostMapping("/members/sign-up/email-check")
    public String emailCheckAndSaveMember(SignUpDto signUpDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(signUpDto.getAuthKey(),
                signUpDto.getAuthKeyInput(), result)) {
            return "members/sign-up/email-check";
        }

        memberSaveService.saveMember(signUpDto);
        memberViewService.login(signUpDto.toEntity());
        sessionStatus.isComplete();
        return "redirect:/members/sign-up/success";
    }

    /**
     * 회원 가입 성공 페이지로 이동한다.
     */
    @GetMapping("/members/sign-up/success")
    public String signUpSuccess() {
        return "members/sign-up/success";
    }

    /**
     * 비밀번호 찾기 페이지를 요청받으면 이메일 인증 페이지로 이동한다.
     */
    @GetMapping("/members/password-find")
    public String getPasswordFindForm(Model model) {
        model.addAttribute(new SignUpDto());
        return "members/password-find";
    }

    /**
     * 입력 받은 이메일이 존재하는지 검증 후 인증 메일을 전송한다.
     * 세션에 데이터를 담고 인증번호 입력 페이지로 이동한다.
     * 비정상적인 이메일 입력이라도 인증번호 입력 페이지로 이동한다.
     */
    @PostMapping("/members/password-find")
    public String sendEmailAuthKey(HttpSession session, SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        if (memberViewService.existsById(MemberId.of(email, MemberType.NORMAL))) {
            signUpDto.createEmailAuthKey();
            signUpManager.sendAuthMessage(signUpDto.getEmail(), signUpDto.getAuthKey());
            session.setAttribute("memberSaveRequestDto", signUpDto);
            return "redirect:/members/email-check";
        }

        return "redirect:/members/email-check";
    }

    /**
     * 세션에 담겨있는 데이터를 가지고 이메일 인증페이지로 이동한다.
     */
    @GetMapping("/members/email-check")
    public String getEmailAuthKeyForm(Model model, SignUpDto signUpDto) {
        model.addAttribute(signUpDto);
        return "members/email-check";
    }

    /**
     * 이메일 인증이 성공하면 이메일로 새로운 비밀번호를 전송한다.
     * 전송후 비밀번호 변경 완료 페이지로 이동한다.
     */
    @PostMapping("/members/email-check")
    public String sendNewPassword(SignUpDto signUpDto,
            BindingResult result, SessionStatus sessionStatus) {

        if (signUpValidation.validateEmailAuth(signUpDto.getAuthKey(),
                signUpDto.getAuthKeyInput(), result)) {
            return "members/email-check";
        }

        String newPassword = signUpManager.sendNewPassword(signUpDto.getEmail());
        memberSaveService.changePassword(MemberId.of(signUpDto.getEmail(), MemberType.NORMAL),
                        newPassword);

        sessionStatus.isComplete();
        return "redirect:/members/password-success";
    }

    @GetMapping("/members/password-success")
    public String passwordSuccess() {
        return "members/password-success";
    }

}
