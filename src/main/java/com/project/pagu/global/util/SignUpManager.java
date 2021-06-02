package com.project.pagu.global.util;

import com.project.pagu.domain.member.dto.SignUpDto;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 6:25 오후
 */
public interface SignUpManager {

    void sendAuthMessage(String email, String authKey);

    void encryptPassword(SignUpDto signUpDto);

    String sendNewPassword(String email);
}
