package com.project.pagu.modules.member.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.common.manager.FileManager;
import com.project.pagu.modules.board.service.BoardViewService;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.SignUpDto;
import com.project.pagu.modules.member.model.PasswordSaveDto;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.common.validation.ProfileValidation;
import com.project.pagu.modules.member.service.MemberViewService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

    private final MemberSaveService memberSaveService;
    private final MemberViewService memberViewService;
    private final ProfileValidation profileValidation;
    private final BoardViewService boardViewService;
    private final FileManager fileManager;

    @InitBinder("profileDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(profileValidation);
    }

    /**
     * 프로필 페이지로 이동한다.
     * 로그인이 되어있지 않을 경우 로그인페이지로 이동한다.
     */
    @GetMapping("/members/profile")
    public String profile(@CurrentMember Member member, Model model) {
        if (member == null) {
            return "redirect:/login";
        }

        if (member.getMemberType().equals(MemberType.GOOGLE)) {
            member = memberViewService.findById(member.getMemberId());
        }

        model.addAttribute(memberViewService.convertToProfileViewDtoBy(member.getNickname()));
        return "members/profile";
    }

    /**
     * 프로필 수정데이터를 전송받아 수정한다.
     */
    @PostMapping("/members/profile")
    public String updateMember(@CurrentMember Member member, @Valid ProfileDto profileDto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "members/profile";
        }

        memberSaveService.update(member.getMemberId(), profileDto);
        return "redirect:/members/profile";
    }

    /**
     * 다른회원의 프로필 페이지로 이동한다.
     * 본인일 경우 본인의 프로필 페이지로 이동한다.
     */
    @GetMapping("/profile/{nickname}")
    public String getProfile(@CurrentMember Member member, @PathVariable String nickname, Model model) {
        ProfileDto profileViewDto = memberViewService.convertToProfileViewDtoBy(nickname);

        MemberId currentMemberId = member.getMemberId();
        MemberId findMemberId = MemberId.of(profileViewDto.getEmail(), member.getMemberType());
        if (currentMemberId.equals(findMemberId)) {
            return "redirect:/profile";
        }

        model.addAttribute(profileViewDto);
        return "/profile/detail";
    }

    /**
     * 본인 확인을 위해 비밀번호 확인 페이지로 이동한다.
     */
    @GetMapping("/members/password-check/{viewName}")
    public String passwordForm(@CurrentMember Member member, @PathVariable String viewName, Model model) {
        //todo : 구글 계정일 경우 회원탈퇴
//        if (member.getMemberType().equals(MemberType.GOOGLE)) {
//            memberService.deleteMember(member);
//            return "redirect:/members/delete-success";
//        }
        model.addAttribute(new SignUpDto());
        model.addAttribute("view", viewName);
        return "members/password-check";
    }

    /**
     * 비밀번호 확인 후 비밀번호 변경 또는 회원탈퇴 페이지로 이동한다.
     */
    @PostMapping("/members/password-check/{name}")
    public String checkPassword(@CurrentMember Member member, Model model,
            @PathVariable String name, SignUpDto dto, BindingResult result) {
        if (profileValidation.isCurrentMemberPassword(dto.getPassword(), member.getPassword(), result)) {
            model.addAttribute("view", name);
            return "members/password-check";
        }

        if (name.equals("change-password")) {
            return "redirect:/members/change-password";
        }

        if (name.equals("secession")) {
            memberSaveService.deleteMember(MemberId.of(member.getEmail(), member.getMemberType()));
            return "redirect:/members/delete-success";
        }
        return "error";

    }

    /**
     * 비밀번호 변경 페이지로 이동한다.
     */
    @GetMapping("/members/change-password")
    public String changePassword(Model model) {
        model.addAttribute(new PasswordSaveDto());
        return "members/change-password";
    }

    /**
     * 비밀번호 데이터를 받아 비밀번호를 수정한다.
     * 실패시 비밀번호 변경 페이지로 이동한다.
     */
    @PostMapping("/members/change-password")
    public String submitPassword(@CurrentMember Member member, @Valid PasswordSaveDto dto, BindingResult result) {
        profileValidation.isNotEqualToPassword(dto.getPassword(), dto.getPasswordCheck(), result);

        if (result.hasErrors()) {
            return "members/change-password";
        }

        memberSaveService.changePassword(MemberId.of(member.getEmail(), member.getMemberType()),
                dto.getPassword());
        memberViewService.login(member);

        return "redirect:/members/password-success";
    }

    /**
     * 회원탈퇴 성공페이지로 이동한다.
     */
    @GetMapping("/members/delete-success")
    public String deleteSuccess() {
        return "members/delete-success";
    }

    /**
     * 프로필에서 게시물 관리 페이지로 이동한다.
     */
    @GetMapping("/profile/boards")
    public String profileBoards(@CurrentMember Member member,
            @PageableDefault(sort = "modifiedDate", direction = Direction.DESC) final Pageable pageable,
            Model model) {

        model.addAttribute("boards", boardViewService.getPagedBoardListByMemberId(member, pageable));

        return "profile/boards";
    }

    /**
     * 프로필의 썸네일 이미지를 가져온다.
     */
    @GetMapping("/profileThumbnails/{type}/{email}/{filename}")
    public void profileThumbnails(@PathVariable String type,
            @PathVariable String email,
            @PathVariable String filename,
            HttpServletResponse response) throws Exception {
        fileManager.profileThumbnails(response, filename, type, email);
    }

}