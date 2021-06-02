package com.project.pagu.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-20 Time: 오후 3:44
 */

@Getter
@RequiredArgsConstructor
public enum StudyStatus {
    READY(0,"모집중"),
    FINISH(1,"모집마감");

    private final int key;
    private final String statusName;
}
