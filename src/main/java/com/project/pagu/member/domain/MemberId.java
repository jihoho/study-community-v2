package com.project.pagu.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:11
 */


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberId implements Serializable {

    private String email;
    private MemberType memberType;

    public static MemberId of(String email, MemberType memberType) {
        return new MemberId(email, memberType);
    }

}
