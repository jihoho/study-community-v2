package com.project.pagu.config.auth.dto;

import com.project.pagu.domain.member.Member;
import com.project.pagu.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01 Time: 오후 11:19
 */

@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map 이기 때문에 값 하나하나를 변환해야만 함.
    public static OAuthAttributes of(String registartionId, String userNameAttributeName, Map<String ,Object> attributes){
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /*
         User 엔티티를 생성함
         OAuthAttributes에서 엔티티를 생성하는 시점은 처음
         가입할때의 기본권한을 GUEST로 주기 위해서 role빌더값에는 Role.GUSET 를 사용.
         OAuthAttributes 클래스 생성이 끝났으면 같은 패키지에 SessionUser 클래스를 생성
     */
    public Member toEntity(){
        return Member.builder()
                .nickname(name)
                .email(email)
                .role(Role.GUEST)
                .build();
    }
}
