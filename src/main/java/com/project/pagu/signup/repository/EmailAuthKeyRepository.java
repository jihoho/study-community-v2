package com.project.pagu.signup.repository;

import com.project.pagu.signup.domain.EmailAuthKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 3:37
 */
public interface EmailAuthKeyRepository extends JpaRepository<EmailAuthKey, String> {
}
