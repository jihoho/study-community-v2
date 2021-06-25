package com.project.pagu.infra.oauth;

import com.project.pagu.domain.member.domain.Member;
import com.project.pagu.domain.model.MemberType;
import com.project.pagu.domain.model.Role;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01Time: 오후 11:19
 */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthFactory {

    public static Member of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        }
        throw new NoSuchElementException();
    }

    private static Member ofGoogle(Map<String, Object> attributes) {
        return Member.builder()
                .email((String) attributes.get("email"))
                .memberType(MemberType.GOOGLE)
                .oauthImageUrl((String) attributes.get("picture"))
                .role(Role.GUEST)
                .build();
    }

}
