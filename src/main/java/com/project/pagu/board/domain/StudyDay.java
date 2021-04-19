package com.project.pagu.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-19 Time: 오후 11:29
 */
@Getter
@RequiredArgsConstructor
public enum StudyDay {
    MON(0, "월요일"),
    TUES(1, "화요일"),
    WED(2, "수요일"),
    THURS(3, "목요일"),
    FRI(4, "금요일"),
    SAT(5, "토요일"),
    SUN(6, "일요일");

    private final int key;
    private final String dayName;
}
