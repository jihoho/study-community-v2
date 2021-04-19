package com.project.pagu.handler;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.service.MemberService;
import com.project.pagu.oauth.model.OauthMember;
import java.io.IOException;
import java.util.Optional;
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
            Authentication authentication) throws IOException{
        OauthMember oauthMember = (OauthMember) authentication.getPrincipal();

        Optional<Member> findMember = memberService
                .findById(new MemberId(oauthMember.getMember().getEmail(),
                        oauthMember.getMember().getMemberType()));

        if (findMember.isPresent()) {
            Member member = findMember.get();
            log.info(String.format("OAuth2 login success email: %s, type: %s ", member.getEmail(),
                    member.getMemberType()));
            response.sendRedirect("/");
        } else { // 처음 회원가입 시 닉네임 등록 페이지로 이동
            log.info("Nickname registration required");
            response.sendRedirect("/sign-up-google");
        }

    }
}
