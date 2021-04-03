package com.project.pagu.domain.member;

import com.project.pagu.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    private String imageURL;

    private String link;

    @Lob
    private String info;

    private String career;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String email, MemberType memberType, String nickname,
                  String password, String imageURL, Role role) {
        this.email = email;
        this.memberType = memberType;
        this.password = password;
        this.nickname = nickname;
        this.imageURL = imageURL;
        this.role = role;
    }

    @Builder
    public Member(String email,MemberType memberType,String nickname,
                  String password,Role role){
        this.email=email;
        this.memberType=memberType;
        this.nickname=nickname;
        this.password=password;
        this.role=role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getMemberTypeKey() {
        return this.memberType.getKey();
    }
}
