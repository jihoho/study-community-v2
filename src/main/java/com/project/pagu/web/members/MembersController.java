package com.project.pagu.web.members;

import com.project.pagu.domain.member.MemberId;
import com.project.pagu.domain.validation.MemberSaveValidator;
import com.project.pagu.service.email.AuthMailService;
import com.project.pagu.service.members.MembersService;
import com.project.pagu.web.dto.AuthMailSaveDto;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:06 오후
 */

@Controller
@RequiredArgsConstructor
public class MembersController {
    private final MemberSaveValidator memberSaveValidator;
    private final MembersService membersService;
    private final AuthMailService authMailService;

    @GetMapping("profile")
    public String profile(/* @AuthenticationPrincipal Member member */) {
        /**
         * 로그인 상태
         */
        return "profile";
    }

    @PostMapping("/members/valid")
    public String validMember(@Valid MemberSaveRequestDto memberSaveRequestDto, BindingResult result, Model model) {
        memberSaveValidator.validate(memberSaveRequestDto, result);
        if (result.hasErrors()) {
            System.out.println(result);
            return "sign-up";
        }
        String email = memberSaveRequestDto.getEmail();
        String authKey = authMailService.sendMessage(email);
        AuthMailSaveDto authMailSaveDto
                = AuthMailSaveDto.builder().email(email).authKey(authKey).build();
        authMailService.save(authMailSaveDto);
        model.addAttribute(memberSaveRequestDto);
        return "email-check";
    }

    @PostMapping("/members/email-check")
    public String addMember(MemberSaveRequestDto memberSaveRequestDto, BindingResult result) {

        System.out.println(memberSaveRequestDto.toString());
        memberSaveValidator.validate(memberSaveRequestDto, result);
        if (result.hasErrors()) {
            System.out.println(result);
            return "sign-up";
        }
        MemberId memberId = membersService.save(memberSaveRequestDto);

        return "email-check";
    }

}
