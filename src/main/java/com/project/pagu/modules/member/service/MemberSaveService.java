package com.project.pagu.modules.member.service;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.model.OauthDto;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.model.SignUpDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberSaveService {

    MemberId saveMember(SignUpDto signUpDto);

    Member saveMember(OauthDto OAuthDto);

    Member save(Member member);

    void update(Member findMember, ProfileDto profileDto);

    void changePassword(MemberId of, String newPassword);

    void deleteMember(MemberId memberId);
}
