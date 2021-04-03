package com.project.pagu.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 5:13
 */
@Getter
@RequiredArgsConstructor
public enum MemberType {
    NORMAL("TYPE_NORMAL", "일반 계정"),
    GOOGLE("TYPE_GOOGLE", "구글 계정");

    private final String key;
    private final String title;
}
