package com.project.pagu.signup.model;

import com.project.pagu.annotation.ValidAuthKey;
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
@ValidAuthKey(
        idField = "email",
        authKeyField = "authKey"
)
public class EmailAuthKeyDto {

    String email;
    String authKey;
}
