package com.project.pagu.modules.board.model;

import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.member.domain.Member;
import com.project.pagu.modules.member.domain.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-26 Time: 오후 8:16
 */

@Builder
@Getter
@Setter
public class WriterDto {

    private String email;

    private MemberType memberType;

    private String imageUrl;

    private String nickname;

    public static WriterDto createWriterDto(Member member) {
        return WriterDto.builder()
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .imageUrl(member.getProfileImageUrl())
                .nickname(member.getNickname()).build();
    }
}
