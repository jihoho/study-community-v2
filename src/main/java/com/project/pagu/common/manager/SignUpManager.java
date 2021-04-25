package com.project.pagu.common.manager;

import com.project.pagu.modules.member.model.MemberSaveRequestDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 6:25 오후
 */
public interface SignUpManager {

    void sendAuthMessage(String email, String authKey);

    void encryptPassword(MemberSaveRequestDto memberSaveRequestDto);

}
