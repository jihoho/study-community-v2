package com.project.pagu.service.email;

import com.project.pagu.domain.email.EmailAuthKey;
import com.project.pagu.domain.email.EmailAuthKeyRepository;
import com.project.pagu.web.dto.EmailAuthKeyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 2:02
 */


@Service
@RequiredArgsConstructor
public class EmailAuthKeyService {
    private final JavaMailSender emailSender;
    private final EmailAuthKeyRepository emailAuthKeyRepository;

    private final PasswordEncoder authKeyEncoder;


    private static final String MAIL_SUBJECT = "PAGU 인증 이메일 입니다.";
    private static final String MAIL_CONTENT = "인증번호는 %s 입니다.";


    private String getKey(int size) {
        return getAuthCode(size);
    }

    private String getAuthCode(int size) {
        Random rand = new Random();
        StringBuffer buffer = new StringBuffer();
        int num;
        while (buffer.length() < size) {
            num = rand.nextInt(10);
            buffer.append(num);
        }
        return buffer.toString();
    }

    @Transactional
    public String sendMessage(String to) {
        String authKey = getKey(6);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jihoho4589@gmail.com");
        message.setTo(to);
        message.setSubject(MAIL_SUBJECT);
        message.setText(String.format(MAIL_CONTENT, authKey));
        emailSender.send(message);
        return authKey;
    }

    @Transactional
    public String save(EmailAuthKeyDto emailAuthKeyDto) {
        String email = emailAuthKeyDto.getEmail();
        String authKey = authKeyEncoder.encode(emailAuthKeyDto.getAuthKey());
        System.out.println("encoding auth key: " + authKey);
        EmailAuthKey emailAuthKey = EmailAuthKey.builder().email(email).authKey(authKey).build();
        emailAuthKeyRepository.save(emailAuthKey);
        return email;
    }

    @Transactional
    public Optional<EmailAuthKey> findById(String email) {
        return emailAuthKeyRepository.findById(email);
    }


}
