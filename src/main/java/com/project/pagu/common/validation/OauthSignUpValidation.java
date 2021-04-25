package com.project.pagu.common.validation;

import com.project.pagu.modules.member.model.OauthMemberSaveDto;
import com.project.pagu.modules.member.service.MemberService;
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

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(OauthMemberSaveDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OauthMemberSaveDto oAuthMemberSaveDto = (OauthMemberSaveDto) target;
        isExistedNickname(oAuthMemberSaveDto.getNickname(), errors);
    }

    private void isExistedNickname(String nickname, Errors errors) {
        if (memberService.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "UniqueNickname", "이미 존재하는 닉네임입니다.");
        }
    }
}
