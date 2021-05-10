package com.project.pagu.modules.member.service;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.model.OauthSaveDto;
import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.model.SignUpDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberSaveService {

    //todo id리턴이 필요할때 변경
    void saveMember(SignUpDto signUpDto);

    void saveMember(OauthSaveDto OAuthSaveDto);

    void update(MemberId memberId, ProfileDto profileDto);

    void changePassword(MemberId memberId, String newPassword);

    void deleteMember(MemberId memberId);
}
