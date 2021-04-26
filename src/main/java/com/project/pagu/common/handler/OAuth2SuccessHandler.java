package com.project.pagu.common.handler;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.service.MemberService;
import com.project.pagu.modules.member.domain.OauthMember;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-15 Time: 오후 3:11
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private static final String HOME = "/";
    private static final String SIGN_UP = "/sign-up-google";

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OauthMember oauthMember = (OauthMember) authentication.getPrincipal();
        String email = oauthMember.getMember().getEmail();
        MemberType memberType = oauthMember.getMember().getMemberType();

        response.sendRedirect(getRedirectUrl(email, memberType));
    }

    private String getRedirectUrl(String email, MemberType memberType) {
        if (memberService.existsById(MemberId.of(email, memberType))) {
            log.info(String.format("OAuth2 login success email: %s, type: %s ", email, memberType));
            return HOME;
        }
        return SIGN_UP;
    }

}
