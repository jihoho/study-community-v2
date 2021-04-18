package com.project.pagu.member.model;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

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

    private String email;

    private String memberType;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$",
            message = "2글자 이상 8글자 이하, 공백을 포함 할 수 없으며 특수문자는 '-','_'만 가능합니다.")
    private String nickname;

    private MultipartFile multipartFile;

    private String imageFile;

    private String imageUrl;

    private String link;

    private String info;

    private String career;

    private String position;

}
