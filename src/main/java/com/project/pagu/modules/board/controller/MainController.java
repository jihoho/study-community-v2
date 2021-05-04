package com.project.pagu.modules.board.controller;

import com.project.pagu.common.annotation.CurrentMember;
import com.project.pagu.modules.board.service.BoardViewService;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.model.OauthMemberSaveDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.common.validation.OauthSignUpValidation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/03/31 Time: 5:44 오후
 */

@RequiredArgsConstructor
@Controller
public class MainController {

    private static final String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final MemberSaveService memberSaveService;
    private final OauthSignUpValidation oauthSignUpValidation;
    private final BoardViewService boardViewService;

    @InitBinder("oauthMemberSaveDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(oauthSignUpValidation);
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("boardList", boardViewService.getLatestBoard(10));
        return "main-body";
    }

    @GetMapping("/sign-up-google")
    public String signUpGoogle(Model model) {
        model.addAttribute(new OauthMemberSaveDto());
        return "sign-up-google";
    }

    @PostMapping("/sign-up-google")
    public String submitSignUpGoogle(@CurrentMember Member member,
            @Valid OauthMemberSaveDto oAuthMemberSaveDto,
            BindingResult result) {
        if (result.hasErrors()) {
            return "sign-up-google";
        }

        oAuthMemberSaveDto.updateEmailAndImage(member.getEmail(), member.getOauthImageUrl());

        memberSaveService.saveMember(oAuthMemberSaveDto);
        return "redirect:/";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("auth-login")
    public String getLoginPage(Model model) throws Exception {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        assert clientRegistrations != null;
        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "auth/oauth-login";
    }
}
