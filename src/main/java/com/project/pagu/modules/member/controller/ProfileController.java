package com.project.pagu.modules.member.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import com.project.pagu.modules.member.model.PasswordDto;
import com.project.pagu.modules.member.model.ProfileRequestDto;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.common.validation.ProfileValidation;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/16 Time: 4:02 오후
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final MemberService memberService;
    private final ProfileValidation profileValidation;
    private final FileManager fileManager;

    @InitBinder("profileRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(profileValidation);
    }

    @GetMapping("/profile")
    public String profile(@CurrentMember Member member, Model model) {
        ProfileRequestDto profileRequestDto = memberService
                .convertMemberToProfileRequestDto(member);
        model.addAttribute(profileRequestDto);
        return "profile";
    }

    @PostMapping("/members/update")
    public String updateMember(@CurrentMember Member member,
            @Valid ProfileRequestDto profileRequestDto, BindingResult result) {

        if (result.hasErrors()) {
            return "profile";
        }

        memberService.update(member, profileRequestDto);
        return "redirect:/profile";
    }

    @GetMapping("/profileThumbnails/{type}/{email}/{filename}")
    public void profileThumbnails(@PathVariable String type,
            @PathVariable String email,
            @PathVariable String filename,
            HttpServletResponse response) throws Exception {
        fileManager.profileThumbnails(response, filename, type, email);
    }

    @GetMapping("/profile/{nickname}")
    public String getProfile(@CurrentMember Member member, @PathVariable String nickname, Model model) {
        ProfileRequestDto profileRequestDto = memberService.getBy(nickname);
        //자기 자신을 조회하면 프로필 관리로 이동
        MemberId currentMemberId = MemberId.of(member.getEmail(), member.getMemberType());
        MemberId findMemberId = MemberId.of(profileRequestDto.getEmail(), member.getMemberType());
        if (currentMemberId.equals(findMemberId)) {
            return "redirect:/profile";
        }

        model.addAttribute(profileRequestDto);
        return "/profile/detail";
    }

    @GetMapping("/members/password-check/{name}")
    public String passwordForm(@CurrentMember Member member, @PathVariable String name, Model model) {
        //todo : 구글 계정일 경우 회원탈퇴
//        if (member.getMemberType().equals(MemberType.GOOGLE)) {
//            memberService.deleteMember(member);
//            return "redirect:/members/delete-success";
//        }
        model.addAttribute(new MemberSaveRequestDto());
        model.addAttribute("view", name);
        return "members/password-check";
    }

    @PostMapping("/members/password-check/{name}")
    public String checkPassword(@CurrentMember Member member, @PathVariable String name, MemberSaveRequestDto dto, BindingResult result) {
        if (profileValidation.isCurrentMemberPassword(dto.getPassword(), member.getPassword(), result)) {
            return "members/password-check";
        }

        if (name.equals("change-password")) {
            return "redirect:/members/change-password";
        }

        if (name.equals("secession")) {
            memberService.deleteMember(member);
            return "redirect:/members/delete-success";
        }
        return "/error";

    }

    @GetMapping("/members/change-password")
    public String changePassword(Model model) {
        model.addAttribute(new PasswordDto());
        return "members/change-password";
    }

    @PostMapping("/members/change-password")
    public String submitPassword(@CurrentMember Member member, @Valid PasswordDto dto, BindingResult result) {
        profileValidation.isNotEqualToPassword(dto.getPassword(), dto.getPasswordCheck(), result);

        if (result.hasErrors()) {
            return "members/change-password";
        }

        memberService.changePassword(MemberId.of(member.getEmail(), member.getMemberType()), dto.getPassword());
        memberService.login(member);

        return "redirect:/members/password-success";
    }

    @GetMapping("/members/delete-success")
    public String deleteSuccess() {
        return "members/delete-success";
    }
}
