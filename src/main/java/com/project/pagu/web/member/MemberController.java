package com.project.pagu.web.member;

import com.project.pagu.web.dto.MemberSaveRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/01 Time: 3:06 오후
 */

@Controller
public class MemberController {

    @GetMapping("profile")
    public String profile(/* @AuthenticationPrincipal Member member */) {
        /**
         * 로그인 상태
         */
        return "profile";
    }

    @PostMapping("/members")
    public String addMember(@Valid MemberSaveRequestDto memberSaveRequestDto, BindingResult result){
        System.out.println("asd");
        System.out.println(memberSaveRequestDto.getMemberId());
        if (result.hasErrors()){
            System.out.println(result);
            return "sign-up";
        }
        return "email-check";
    }

}
