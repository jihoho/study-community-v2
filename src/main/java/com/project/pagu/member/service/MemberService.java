package com.project.pagu.member.service;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.OauthMemberSaveDto;
import com.project.pagu.member.model.ProfileRequestDto;
import com.project.pagu.member.model.MemberSaveRequestDto;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberService {

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
