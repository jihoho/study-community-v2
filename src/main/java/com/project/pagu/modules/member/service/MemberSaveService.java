package com.project.pagu.modules.member.service;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.model.OauthMemberSaveDto;
import com.project.pagu.modules.member.model.ProfileRequestDto;
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberSaveService {

    MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto);

    Member saveMember(OauthMemberSaveDto OAuthMemberSaveDto);

    void update(Member findMember, ProfileRequestDto profileRequestDto);

    void changePassword(MemberId of, String newPassword);

    void deleteMember(MemberId memberId);
}
