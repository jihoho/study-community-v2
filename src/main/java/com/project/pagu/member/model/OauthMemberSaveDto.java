package com.project.pagu.member.model;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/14 Time: 8:38 오후
 */

@Getter
@Setter
public class OauthMemberSaveDto {

    private String email;
    private String nickname;
    private String filename;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .memberType(MemberType.GOOGLE)
                .filename(filename)
                .nickname(nickname)
                .role(Role.GUEST)
                .build();
    }

    public void updateEmailAndImage(String email, String filename) {
        this.email = email;
        this.filename = filename;
    }
}
