package com.project.pagu.domain.member.domain;

import com.project.pagu.domain.board.domain.Board;
import com.project.pagu.domain.comment.domain.Comment;
import com.project.pagu.domain.model.MemberId;
import com.project.pagu.domain.model.MemberType;
import com.project.pagu.domain.model.Role;
import com.project.pagu.global.util.FileUtil;
import com.project.pagu.global.domain.BaseTimeEntity;
import com.project.pagu.domain.member.dto.ProfileDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

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
@Where(clause = "is_delete = false")
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

    private String info;

    private String career;

    private String position;

    private boolean isDelete;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String email, MemberType memberType, String nickname, String password, String imageFilename,
            String oauthImageUrl, String link, String info, String career, String position, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.nickname = nickname;
        this.password = password;
        this.imageFilename = imageFilename;
        this.oauthImageUrl = oauthImageUrl;
        this.link = link;
        this.info = info;
        this.career = career;
        this.position = position;
        this.role = role;
        choiceImageUrl(email, imageFilename, oauthImageUrl, memberType.getKey());
    }

    private void choiceImageUrl(String email, String imageFilename, String oauthImageUrl, String key) {
        if (imageFilename != null && !imageFilename.isBlank()) {
            this.profileImageUrl = FileUtil.createImageUrl(key, email, imageFilename);
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

    public void updateProfile(ProfileDto dto) {
        this.nickname = dto.getChangeNickname();
        this.oauthImageUrl = dto.getOauthImageUrl();
        this.imageFilename = dto.getImageFilename();
        this.link = dto.getLink();
        this.info = dto.getInfo();
        this.career = dto.getCareer();
        this.position = dto.getPosition();
        this.role = Role.USER;
        choiceImageUrl(dto.getEmail(), imageFilename, oauthImageUrl, dto.getMemberType());
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


    public void changePassword(String password) {
        this.password = password;
    }

    public void delete() {
        this.isDelete = true;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
