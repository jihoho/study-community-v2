package com.project.pagu.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-01 Time: 오후 11:24
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
