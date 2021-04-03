package com.project.pagu.web.dto;

import com.project.pagu.domain.member.Member;
import com.project.pagu.domain.member.MemberType;
import com.project.pagu.domain.member.Role;
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
public class MemberSaveRequestDto {

    @NotBlank
    @Email(message = "이메일 형식을 맞춰주세요.")
    String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$",
            message = "2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.")
    String nickname;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    String password;

    @NotBlank
    String passwordCheck;

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
