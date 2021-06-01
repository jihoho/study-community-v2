package com.project.pagu.modules.member.model;

import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.domain.Role;
import java.util.UUID;
import lombok.*;

import javax.validation.constraints.*;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:44
 */

@Getter
@Setter
public class SignUpDto {

    @NotBlank
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
            message = "올바른 형식의 이메일을 입력해주세요.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,8}$",
            message = "2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank
    private String passwordCheck;

    private String authKey;

    private String authKeyInput;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .memberType(MemberType.NORMAL)
                .nickname(nickname)
                .password(password)
                .role(Role.GUEST)
                .build();
    }

    public void createEmailAuthKey() {
        this.authKey = UUID.randomUUID().toString();
        System.out.printf("회원 : %s, 인증 키 : %s\n",this.email,this.authKey);
    }
}
