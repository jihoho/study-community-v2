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

    private OAuthAttributes() {}

    public static OauthMember of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        }
        throw new NoSuchElementException();
    }

    private static OauthMember ofGoogle(Map<String, Object> attributes) {
        return new OauthMember(Member.builder()
                .email((String) attributes.get("email"))
                .filename((String) attributes.get("picture"))
                .memberType(MemberType.GOOGLE)
                .role(Role.GUEST)
                .build());
    }
}
