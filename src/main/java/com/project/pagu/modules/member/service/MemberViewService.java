package com.project.pagu.modules.member.service;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.model.ProfileDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/05/03 Time: 2:28 오후
 */
public interface MemberViewService extends UserDetailsService,
        OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    Member findById(MemberId memberId);

    boolean existsById(MemberId memberId);

    boolean existsByNickname(String nickname);

    ProfileDto convertToProfileViewDtoBy(String nickname);

    void login(Member member);
}
