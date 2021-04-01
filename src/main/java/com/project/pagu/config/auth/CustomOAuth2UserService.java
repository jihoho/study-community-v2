package com.project.pagu.config.auth;

import com.project.pagu.config.auth.dto.OAuthAttributes;
import com.project.pagu.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01 Time: 오후 11:14
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { OAuth2UserService<OAuth2UserRequest,OAuth2User>
            delegage=new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegage.loadUser(userRequest);

        String registrationId =userRequest.getClientRegistration().getRegistrationId(); //1
        String userNameAttributeName= userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); //2
        OAuthAttributes attributes= OAuthAttributes.of(registrationId ,userNameAttributeName,oAuth2User.getAttributes()); //3



        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.GUEST.getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}
