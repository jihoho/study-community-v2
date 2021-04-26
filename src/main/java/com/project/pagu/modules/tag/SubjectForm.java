package com.project.pagu.modules.tag;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/22 Time: 7:07 오후
 */

@Getter
@Setter
public class SubjectForm {

    private String name;

    public Subject toEntity() {
        return Subject.builder()
                .name(name)
                .build();
    }
}
