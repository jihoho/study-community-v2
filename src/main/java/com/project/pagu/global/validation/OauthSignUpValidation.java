package com.project.pagu.global.validation;

import com.project.pagu.domain.member.dto.OauthSaveDto;
import com.project.pagu.domain.member.service.MemberViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/15 Time: 11:13 오전
 */

@RequiredArgsConstructor
@Configuration
public class OauthSignUpValidation implements Validator {

    private final MemberViewService memberViewService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(OauthSaveDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OauthSaveDto oAuthSaveDto = (OauthSaveDto) target;
        isExistedNickname(oAuthSaveDto.getNickname(), errors);
    }

    private void isExistedNickname(String nickname, Errors errors) {
        if (memberViewService.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "UniqueNickname", "이미 존재하는 닉네임입니다.");
        }
    }
}
