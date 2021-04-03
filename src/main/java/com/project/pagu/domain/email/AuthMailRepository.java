package com.project.pagu.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 3:37
 */
public interface AuthMailRepository extends JpaRepository<AuthMail,String> {
}
