package com.project.pagu.signup;

import com.project.pagu.member.model.MemberSaveRequestDto;
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
    public void sendMessageToMemberDto(MemberSaveRequestDto memberSaveRequestDto) {
        memberSaveRequestDto.createEmailAuthKey();
        SimpleMailMessage simpleMessage = createMessage(memberSaveRequestDto.getEmail(), memberSaveRequestDto.getAuthKey());
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
    public void encryptPassword(MemberSaveRequestDto memberSaveRequestDto) {
        String password = memberSaveRequestDto.getPassword();
        memberSaveRequestDto.setPassword(passwordEncoder.encode(password));
        memberSaveRequestDto.setPasswordCheck(passwordEncoder.encode(password));
    }

}
