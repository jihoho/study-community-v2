package com.project.pagu.modules.member.domain;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-09 Time: 오전 1:15
 */
@Getter
public class UserMember extends User {
    private Member member;

    public UserMember(Member member){
        super(member.getEmail(),member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRoleKey())));
        this.member=member;
    }
}
