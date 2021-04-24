package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.OauthMemberSaveDto;
import com.project.pagu.member.model.ProfileRequestDto;
import com.project.pagu.member.model.MemberSaveRequestDto;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberService extends UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    boolean existsById(MemberId memberId);

    boolean existsByNickname(String nickname);

    Member findById(MemberId memberId);

    void login(Member member);

    MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto);

    Member save(Member member);

    ProfileRequestDto convertMemberToProfileRequestDto(Member member);

    Member saveMember(OauthMemberSaveDto OAuthMemberSaveDto);

    void update(Member findMember, ProfileRequestDto profileRequestDto);

}
