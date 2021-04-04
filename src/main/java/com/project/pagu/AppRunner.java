package com.project.pagu;

import com.project.pagu.service.email.EmailAuthKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 2:08
 */

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private PasswordEncoder authKeyEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
