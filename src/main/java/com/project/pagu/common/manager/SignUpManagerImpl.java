package com.project.pagu.common.manager;

import com.project.pagu.modules.member.model.SignUpDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/07 Time: 7:12 오후
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SignUpManagerImpl implements SignUpManager {
    private static final String MAIL_FROM = "jihoho4589@gmail.com";
    private static final String MAIL_SUBJECT = "PAGU 인증 이메일 입니다.";
    private static final String MAIL_CONTENT = "인증번호는 %s 입니다.";
    private static final String INFO = "email address : %s, auth key : %s";

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender memberMailSender;

    @Override
    public void sendAuthMessage(String email, String authKey) {
        SimpleMailMessage simpleMessage = createMessage(email, authKey);
        memberMailSender.send(simpleMessage);
    }

    private SimpleMailMessage createMessage(String email, String authKey) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_FROM);
        message.setTo(email);
        message.setSubject(MAIL_SUBJECT);
        message.setText(String.format(MAIL_CONTENT, authKey));

        log.info(String.format(INFO, email, authKey));
        return message;
    }

    @Override
    public void encryptPassword(SignUpDto signUpDto) {
        String password = signUpDto.getPassword();
        signUpDto.setPassword(passwordEncoder.encode(password));
        signUpDto.setPasswordCheck(signUpDto.getPassword());
    }

    @Override
    public String sendNewPassword(String email) {
        // todo: createMessage() 메소드 재사용 할 수 있게 리팩토링
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_FROM);
        message.setTo(email);
        message.setSubject(MAIL_SUBJECT);
        String newPassword = UUID.randomUUID().toString();
        message.setText(newPassword);

        log.info("new password" + email + newPassword);

        memberMailSender.send(message);

        return newPassword;
    }

}
