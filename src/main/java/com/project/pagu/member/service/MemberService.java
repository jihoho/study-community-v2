package com.project.pagu.member.service;

import com.project.pagu.member.domain.MemberId;
import com.project.pagu.member.model.MemberSaveRequestDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 5:59 오후
 */
public interface MemberService {

    boolean existsByMemberId(MemberId memberId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    MemberId saveMember(MemberSaveRequestDto memberSaveRequestDto);

    void sendMessageToMemberDto(MemberSaveRequestDto memberSaveRequestDto);

    void encryptPassword(MemberSaveRequestDto memberSaveRequestDto);

}
