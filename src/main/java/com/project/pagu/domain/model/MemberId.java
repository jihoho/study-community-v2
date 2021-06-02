package com.project.pagu.domain.model;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MemberId memberId = (MemberId) obj;
        return email.equals(memberId.email) && memberType == memberId.memberType;
    }

}
