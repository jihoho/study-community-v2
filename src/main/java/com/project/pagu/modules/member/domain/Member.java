package com.project.pagu.modules.member.domain;

import com.project.pagu.common.manager.FileUtil;
import com.project.pagu.modules.board.domain.Board;
import com.project.pagu.common.domain.BaseTimeEntity;
import com.project.pagu.modules.member.model.ProfileRequestDto;
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

    private String imageFilename;

    private String oauthImageUrl;

    private String profileImageUrl;

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

    @Builder
    public Member(String email, MemberType memberType, String nickname, String password,
            String imageFilename, String oauthImageUrl, String link, String info, String career,
            String postion,
            Role role) {
        this.email = email;
        this.memberType = memberType;
        this.nickname = nickname;
        this.password = password;
        this.imageFilename = imageFilename;
        this.oauthImageUrl = oauthImageUrl;
        this.link = link;
        this.info = info;
        this.career = career;
        this.postion = postion;
        this.role = role;

        if (imageFilename != null && !imageFilename.equals("")) {
            this.profileImageUrl = FileUtil
                    .createImageUrl("profileThumbnails", memberType.getKey(), email, imageFilename);
        } else {
            this.profileImageUrl = oauthImageUrl;
        }
    }

    public Member updateImage(String imageUrl) {
        if (profileImageUrl.equals(oauthImageUrl)) {
            profileImageUrl = imageUrl;
        }
        oauthImageUrl = imageUrl;
        return this;
    }

    public void updateProfile(ProfileRequestDto dto) {
        this.nickname = dto.getChangeNickname();
        this.oauthImageUrl = dto.getOauthImageUrl();
        this.imageFilename = dto.getImageFilename();
        if (imageFilename != null && !imageFilename.equals("")) {
            profileImageUrl = FileUtil
                    .createImageUrl("profileThumbnails", dto.getMemberType(), dto.getEmail(),
                            imageFilename);
        } else {
            profileImageUrl = oauthImageUrl;
        }
        this.link = dto.getLink();
        this.info = dto.getInfo();
        this.career = dto.getCareer();
        this.postion = dto.getPosition();
        this.role = Role.USER;
    }

    public void addBoard(Board board){
        boards.add(board);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getMemberTypeKey() {
        return this.memberType.getKey();
    }

    public MemberId getMemberId() {
        return MemberId.of(email, memberType);
    }
}
