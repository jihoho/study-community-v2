package com.project.pagu.web.dto;

import lombok.*;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 3:45
 */

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmailAuthKeyDto {
    String email;
    String authKey;
}
