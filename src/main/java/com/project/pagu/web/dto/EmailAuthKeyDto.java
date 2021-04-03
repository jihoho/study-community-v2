package com.project.pagu.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 3:45
 */

@AllArgsConstructor
@Builder
@Getter
public class EmailAuthKeyDto {
    String email;
    String authKey;
}
