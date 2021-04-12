package com.project.pagu.member.domain;

import com.project.pagu.common.BaseTimeEntity;
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

    private String filename;

    private String link;

    @Lob
    private String info;

    private String career;

    private String postion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String email, MemberType memberType, String nickname,
            String password, String filename, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.password = password;
        this.nickname = nickname;
        this.filename = filename;
        this.role = role;
    }

    @Builder
    public Member(String email, MemberType memberType, String nickname,
            String password, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getMemberTypeKey() {
        return this.memberType.getKey();
    }
}
