package com.project.pagu.modules.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/22 Time: 7:07 오후
 */

@Getter
@Setter
@Builder
public class SubjectDto {

    private String name;

    public Subject toEntity() {
        return Subject.builder()
                .name(name)
                .build();
    }

    public static SubjectDto of(String name) {
        return SubjectDto.builder().name(name).build();
    }
}
