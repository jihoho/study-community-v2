package com.project.pagu.domain.validation;

import com.project.pagu.domain.email.AuthMail;
import com.project.pagu.service.email.AuthMailService;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 4:28
 */
@Component
@RequiredArgsConstructor
public class AuthEmailValidator implements Validator {
    private final AuthMailService authMailService;
    private final PasswordEncoder authKeyEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberSaveRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberSaveRequestDto memberSaveRequestDto=(MemberSaveRequestDto) target;
        String email=memberSaveRequestDto.getEmail();
        String authKey=memberSaveRequestDto.getAuthKey();
        Optional<AuthMail> optional=authMailService.findById(email);
        try {
            if(optional.isPresent()){
                AuthMail authMail=optional.get();
                LocalDateTime now =LocalDateTime.now();
                LocalDateTime modifiedDate=authMail.getModifiedDate();
                if(now.isBefore(modifiedDate)){
                    throw new Exception("AuthMail.modifiedDate invalid Exception");
                }
                if(ChronoUnit.MINUTES.between(now,modifiedDate)>30){
                    errors.rejectValue("email-check","invalid.authkey.timeout","인증 시간 30분이 초과 되었습니다. 재인증 해주세요.");
                }else if(!authKeyEncoder.matches(authKey,authMail.getAuthKey())){
                    errors.rejectValue("email-check","invalid.authkey.missmatch","인증 번호가 다릅니다.");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
