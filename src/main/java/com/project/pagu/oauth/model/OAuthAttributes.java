package com.project.pagu.oauth.model;

import com.project.pagu.member.domain.Member;
import com.project.pagu.member.domain.MemberType;
import com.project.pagu.member.domain.Role;
import java.util.NoSuchElementException;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01Time: 오후 11:19
 */

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String email;
    private MemberType memberType;
    private String picture;

    private OAuthAttributes(Map<String, Object> attributes, String email, MemberType memberType,
            String picture) {
        this.attributes = attributes;
        this.email = email;
        this.memberType = memberType;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        }
        throw new NoSuchElementException();
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .email((String) attributes.get("email"))
                .memberType(MemberType.GOOGLE)
                .picture((String) attributes.get("picture"))
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .memberType(memberType)
                .imageUrl(picture)
                .role(Role.GUEST)
                .build();
    }
}
