package com.project.pagu.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 2:02
 */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MemberMailSender {

    private static final String MAIL_FROM = "jihoho4589@gmail.com";
    private static final String MAIL_SUBJECT = "PAGU 인증 이메일 입니다.";
    private static final String MAIL_CONTENT = "인증번호는 %s 입니다.";
    private static final String INFO = "email address : %s , auth key : %s";

    private final JavaMailSender mailSender;

    public void sendMessage(String email, String authKey) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_FROM);
        message.setTo(email);
        message.setSubject(MAIL_SUBJECT);
        message.setText(String.format(MAIL_CONTENT, authKey));
        mailSender.send(message);

        log.info(String.format(INFO, email, authKey));
    }

}
