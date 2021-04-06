package com.project.pagu.member.model;

import com.project.pagu.annotation.FieldsValueMatch;
import com.project.pagu.annotation.UniqueEmail;
import com.project.pagu.annotation.UniqueNickname;
import com.project.pagu.annotation.ValidAuthKey;
import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import lombok.*;

import javax.validation.constraints.*;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:44
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "passwordCheck",
                message = "비밀번호가 다릅니다.")
})
@ValidAuthKey(
        authKeyField = "authKey",
        authKeyInputField = "authKeyInput"
)
public class MemberSaveRequestDto {

    @NotBlank
    @Email(message = "이메일 형식을 맞춰주세요.")
    @UniqueEmail(message = "이미 존재하는 이메일입니다.")
    String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$",
            message = "2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.")
    @UniqueNickname(message = "이미 존재하는 닉네임입니다.")
    String nickname;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    String password;

    @NotBlank
    String passwordCheck;

    String authKey;
    String authKeyInput;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .memberType(MemberType.NORMAL)
                .nickname(nickname)
                .password(password)
                .role(Role.GUEST)
                .build();
    }
}
