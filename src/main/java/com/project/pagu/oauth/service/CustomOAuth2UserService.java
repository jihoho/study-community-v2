package com.project.pagu.oauth.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.service.MemberService;
import com.project.pagu.oauth.model.OAuthAttributes;
import com.project.pagu.oauth.model.OauthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01 Time: 오후 11:14
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        // 현재 로그인 진행 중인 서비스
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        return new OauthMember(findMember(attributes));
    }

    private Member findMember(OAuthAttributes attributes) {
        return memberService
                .findById(MemberId.of(attributes.getEmail(), attributes.getMemberType()))
                .updateImage(attributes.getPicture());
    }
}
