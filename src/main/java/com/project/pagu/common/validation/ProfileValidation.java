package com.project.pagu.common.validation;

import com.project.pagu.modules.member.model.ProfileDto;
import com.project.pagu.modules.member.service.MemberViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final MemberViewService memberViewService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProfileDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileDto profileDto = (ProfileDto) target;
        if (!profileDto.getNickname().equals(profileDto.getChangeNickname())) {
            isExistedNickname(profileDto.getChangeNickname(), errors);
        }
    }

    private void isExistedNickname(String nickname, Errors errors) {
        if (memberViewService.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "UniqueNickname", "이미 존재하는 닉네임입니다.");
        }
    }

    public boolean isCurrentMemberPassword(String inputPassword, String memberPassword, Errors errors) {
        if (!passwordEncoder.matches(inputPassword, memberPassword)) {
            errors.rejectValue("password", "NotEqualsPassword", "비밀번호가 일치하지 않습니다.");
            return true;
        }
        return false;
    }

    public void isNotEqualToPassword(String password, String passwordCheck, Errors errors) {
        if (password != null && !password.equals(passwordCheck)) {
            errors.rejectValue("passwordCheck", "NotEqualsPassword", "비밀번호가 다릅니다.");
        }
    }
}
