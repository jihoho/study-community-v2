package com.project.pagu.domain.member;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:11
 */


@Data
public class MemberId implements Serializable {
    private String memberId;
    private MemberType memberType;

}
