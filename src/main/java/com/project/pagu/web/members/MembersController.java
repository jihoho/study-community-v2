package com.project.pagu.web.members;

import com.project.pagu.domain.member.MemberId;
import com.project.pagu.validation.EmailAuthKeyValidator;
import com.project.pagu.service.email.EmailAuthKeyService;
import com.project.pagu.service.members.MembersService;
import com.project.pagu.web.dto.EmailAuthKeyDto;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:06 오후
 */

@Controller
@RequiredArgsConstructor
public class MembersController {
    private final EmailAuthKeyValidator emailAuthKeyValidator;
    private final MembersService membersService;
    private final EmailAuthKeyService emailAuthKeyService;

    @GetMapping("profile")
    public String profile(/* @AuthenticationPrincipal Member member */) {
        /**
         * 로그인 상태
         */
        return "profile";
    }

    @PostMapping("/members/valid")
    public String validMemberSaveForm(@ModelAttribute @Valid MemberSaveRequestDto memberSaveRequestDto,
                                      BindingResult result, HttpServletRequest request, Model model) {
        System.out.println(memberSaveRequestDto.toString());
        if (result.hasErrors()) {
            System.out.println(result);
            return "sign-up";
        }
        String email = memberSaveRequestDto.getEmail();
        String authKey = emailAuthKeyService.sendMessage(email);
        EmailAuthKeyDto emailAuthKeyDto
                = EmailAuthKeyDto.builder().email(email).authKey(authKey).build();
        // save authkey in db
        emailAuthKeyService.save(emailAuthKeyDto);
        membersService.encryptPassword(memberSaveRequestDto);

        // save member info in session
        HttpSession session = request.getSession();
        session.setAttribute("memberInfo", memberSaveRequestDto);
        model.addAttribute(new EmailAuthKeyDto());
        return "email-check";
    }

    @PostMapping("/members/email-check")
    public String emailCheckAndSaveMember(@ModelAttribute EmailAuthKeyDto emailAuthKeyDto,
                                          BindingResult result, HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberSaveRequestDto memberSaveRequestDto = (MemberSaveRequestDto) session.getAttribute("memberInfo");

        emailAuthKeyValidator.validate(emailAuthKeyDto, result);
        System.out.println(result);
        if (result.hasErrors()) {
            System.out.println(result);
            return "email-check";
        }
        MemberId memberId = membersService.save(memberSaveRequestDto);

        return "profile";
    }

}
