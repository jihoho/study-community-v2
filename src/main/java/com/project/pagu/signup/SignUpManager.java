package com.project.pagu.signup;

import com.project.pagu.member.model.MemberSaveRequestDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 6:25 오후
 */
public interface SignUpManager {

    void sendMessageToMemberDto(MemberSaveRequestDto memberSaveRequestDto);

    void encryptPassword(MemberSaveRequestDto memberSaveRequestDto);

}
