package com.project.pagu.oauth.model;

import com.project.pagu.member.domain.Member;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/14 Time: 9:36 오후
 */

@Getter
public class OauthMember implements OAuth2User {

    private Member member;

    public OauthMember(Member member) {
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("email", member.getEmail(),
                "nickname", member.getNickname(),
                "picture", member.getFilename());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRoleKey()));
    }

    @Override
    public String getName() {
        return member.getEmail();
    }
}
