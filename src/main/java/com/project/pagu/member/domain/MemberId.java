package com.project.pagu.member.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:11
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberId implements Serializable {
    private String email;
    private MemberType memberType;

}
