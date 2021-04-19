package com.project.pagu.handler;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.service.MemberService;
import com.project.pagu.oauth.model.OauthMember;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OauthMember oauthMember = (OauthMember) authentication.getPrincipal();
        log.info("oauth2 login email: " + oauthMember.getName());
        // 구글 계정 이미지 변경 됐을 경우 member update
        memberService.findById(
                new MemberId(oauthMember.getMember().getEmail(),
                        oauthMember.getMember().getMemberType())
        ).ifPresentOrElse(
                member -> {
                    log.info("update google picture email: " + member.getEmail());
                    member.updateImage(oauthMember.getMember().getImageUrl());
                    memberService.save(member);
                    try {
                        response.sendRedirect("/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                () -> {
                    try {
                        log.info("OAuth2 Member registration required");
                        response.sendRedirect("/sign-up-google");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
