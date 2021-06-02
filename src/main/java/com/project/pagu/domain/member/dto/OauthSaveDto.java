package com.project.pagu.domain.member.dto;

import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberType;
import com.project.pagu.domain.model.Role;
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
public class OauthSaveDto {

    private String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,8}$",
            message = "2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.")
    private String nickname;

    private String imageUrl;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .memberType(MemberType.GOOGLE)
                .oauthImageUrl(imageUrl)
                .nickname(nickname)
                .role(Role.GUEST)
                .build();
    }

    public void updateEmailAndImage(String email, String imageUrl) {
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
