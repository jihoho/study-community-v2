package com.project.pagu.domain.member;

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
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String imageURL;

    private String link;

    private String info;

    private String career;

    private LocalDateTime create_at;

    private LocalDateTime update_at;

    private LocalDateTime delete_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String nickname,String email,
                  String imageURL,Role role){
        this.nickname=nickname;
        this.email=email;
        this.imageURL=imageURL;
        this.role=role;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
