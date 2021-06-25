package com.project.pagu.domain.techstack.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-29 Time: 오전 12:08
 */

@Getter
@Setter
@Builder
public class TechStackDto {

    private String name;

    public static TechStackDto of(String name) {
        return TechStackDto.builder().name(name).build();
    }
}
