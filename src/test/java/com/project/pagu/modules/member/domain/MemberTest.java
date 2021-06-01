package com.project.pagu.modules.member.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-05-01 Time: 오후 7:23
 */

class MemberTest {

    @Test
    @DisplayName("member 생성 시 profile image url 테스트")
    void create_member() throws Exception {
        // given
        String email = "test@gmail.com";
        MemberType memberType = MemberType.GOOGLE;
        String nickname = "tester";
        String imageFilename = "qweqsdadjlknasndjaksjd";
        String oauthImageUrl = "https://image.png";
        Role role = Role.GUEST;
        String expectedProfileImageUrl1 =
                "/profile/thumbnails/" + memberType.getKey() + "/" + email + "/" + imageFilename;
        String expectedProfileImageUrl2 = oauthImageUrl;
        // when
        String actualUrl1 = Member.builder()
                .email(email)
                .memberType(memberType)
                .nickname(nickname)
                .imageFilename(imageFilename)
                .oauthImageUrl(oauthImageUrl)
                .role(role).build().getProfileImageUrl();
        String actualUrl2 = Member.builder()
                .email(email)
                .memberType(memberType)
                .nickname(nickname)
                .oauthImageUrl(oauthImageUrl)
                .role(role).build().getProfileImageUrl();
        String actualUrl3 = Member.builder()
                .email(email)
                .memberType(memberType)
                .nickname(nickname)
                .role(role).build().getProfileImageUrl();
        // then
        then(actualUrl1).isEqualTo(expectedProfileImageUrl1);
        then(actualUrl2).isEqualTo(expectedProfileImageUrl2);
        then(actualUrl3).isNull();
    }

}