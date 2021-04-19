package com.project.pagu.validation;

import com.project.pagu.member.model.ProfileRequestDto;
import com.project.pagu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/16 Time: 2:45 오후
 */

@RequiredArgsConstructor
@Configuration
public class ProfileValidation implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProfileRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileRequestDto profileRequestDto = (ProfileRequestDto) target;
        if (!profileRequestDto.getNickname().equals(profileRequestDto.getChangeNickname())) {
            isExistedNickname(profileRequestDto.getChangeNickname(), errors);
        }
    }

    private void isExistedNickname(String nickname, Errors errors) {
        if (memberService.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "UniqueNickname", "이미 존재하는 닉네임입니다.");
        }
    }
}
