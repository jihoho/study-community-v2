package com.project.pagu.member.domain;

import com.project.pagu.board.domain.Board;
import com.project.pagu.common.BaseTimeEntity;
import com.project.pagu.member.model.ProfileRequestDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01 Time: 오후 11:22
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(MemberId.class)
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "email")
    private String email;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "member_type")
    private MemberType memberType;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String imageFile;

    private String imageUrl;

    private String link;

    @Lob
    private String info;

    private String career;

    private String postion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    public Member(String email, MemberType memberType, String nickname,
            String password, String imageFile, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.password = password;
        this.nickname = nickname;
        this.imageFile = imageFile;
        this.role = role;
    }

    public Member(String email, MemberType memberType, String nickname,
            String password, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public Member updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void updateProfile(ProfileRequestDto dto) {
        this.nickname = dto.getChangeNickname();
        this.imageUrl = dto.getImageUrl();
        this.imageFile = dto.getImageFile();
        this.link = dto.getLink();
        this.info = dto.getInfo();
        this.career = dto.getCareer();
        this.postion = dto.getPosition();
        this.role = Role.USER;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getMemberTypeKey() {
        return this.memberType.getKey();
    }
}
