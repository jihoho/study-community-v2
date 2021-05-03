package com.project.pagu.common.validation;

import com.project.pagu.modules.member.domain.MemberId;
import com.project.pagu.modules.member.domain.MemberType;
import com.project.pagu.modules.member.model.MemberSaveRequestDto;
import com.project.pagu.modules.member.service.MemberSaveService;
import com.project.pagu.modules.member.service.MemberViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/06 Time: 5:54 오후
 */

@RequiredArgsConstructor
@Configuration
public class SignUpValidation implements Validator {

    private final MemberViewService memberViewService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberSaveRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberSaveRequestDto memberSaveRequestDto = (MemberSaveRequestDto) target;
        isExistedNormalEmail(memberSaveRequestDto.getEmail(), errors);
        isExistedNickname(memberSaveRequestDto.getNickname(), errors);
        isNotEqualToPassword(memberSaveRequestDto.getPassword(), memberSaveRequestDto.getPasswordCheck(), errors);
    }

    private void isExistedNormalEmail(String email,Errors errors) {
        if (memberViewService.existsById(MemberId.of(email,MemberType.NORMAL))) {
            errors.rejectValue("email", "UniqueEmail", "이미 존재하는 이메일입니다.");
        }
    }

    private void isExistedNickname(String nickname, Errors errors) {
        if (memberViewService.existsByNickname(nickname)) {
            errors.rejectValue("nickname", "UniqueNickname", "이미 존재하는 닉네임입니다.");
        }
    }

    private void isNotEqualToPassword(String password, String passwordCheck, Errors errors) {
        if (password != null && !password.equals(passwordCheck)) {
            errors.rejectValue("passwordCheck", "NotEqualsPassword", "비밀번호가 다릅니다.");
        }
    }

    public boolean validateEmailAuth(String authKey, String authKeyInput, Errors errors) {
        if (authKey != null && !authKey.equals(authKeyInput)) {
            errors.rejectValue("authKeyInput", "NotEqualsAuthKeyInput", "인증번호가 다릅니다.");
            return true;
        }
        return false;
    }
}
