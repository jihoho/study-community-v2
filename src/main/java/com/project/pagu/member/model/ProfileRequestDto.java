package com.project.pagu.member.model;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-11 Time: 오후 6:21
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProfileRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String memberType;

    @NotBlank
    private String nickname;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String link;

    @NotBlank
    private String info;

    @NotBlank
    private String career;

    @NotBlank
    private String position;

}
