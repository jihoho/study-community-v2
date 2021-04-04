package com.project.pagu.domain.email;

import com.project.pagu.domain.common.BaseTimeEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 3:27
 */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class EmailAuthKey extends BaseTimeEntity {

    @Id
    private String email;

    private String authKey;

    @Builder
    public EmailAuthKey(String email, String authKey) {
        this.email = email;
        this.authKey = authKey;
    }

}
