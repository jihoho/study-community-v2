package com.project.pagu.domain.member;

import com.project.pagu.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @GeneratedValue
    @Column(name = "member_id")
    private String memberId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "member_type")
    private MemberType memberType;

    private String nickname;

    private String password;

    private String imageURL;

    private String link;

    private String info;

    private String career;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String memberId,MemberType memberType,String nickname,
                  String imageURL,Role role){
        this.memberId=memberId;
        this.memberType=memberType;
        this.nickname=nickname;
        this.imageURL=imageURL;
        this.role=role;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public String getMemberTypeKey(){
        return this.memberType.getKey();
    }
}
