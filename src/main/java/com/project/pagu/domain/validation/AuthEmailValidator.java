package com.project.pagu.domain.validation;

import com.project.pagu.domain.email.AuthMail;
import com.project.pagu.service.email.AuthMailService;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-03 Time: 오후 4:28
 */
@Component
@RequiredArgsConstructor
public class AuthEmailValidator implements Validator {
    private final AuthMailService authMailService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberSaveRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberSaveRequestDto memberSaveRequestDto=(MemberSaveRequestDto) target;
    }
}
