package com.project.pagu.domain.validation;

import com.project.pagu.domain.member.MemberId;
import com.project.pagu.domain.member.MemberType;
import com.project.pagu.service.members.MembersService;
import com.project.pagu.web.dto.MemberSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-02 Time: 오후 9:15
 */
@Component
@RequiredArgsConstructor
public class MemberFormValidator implements Validator {
    private final MembersService membersService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberSaveRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberSaveRequestDto memberSaveRequestDto = (MemberSaveRequestDto) target;
        if (membersService.existsById(new MemberId(memberSaveRequestDto.getEmail(), MemberType.NORMAL))) {
            errors.rejectValue("email", "invalid.email", "이미 사용중인 이메일 입니다.");
        }

        if (membersService.existsByNickname(memberSaveRequestDto.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", "이미 사용중인 닉네임 입니다.");
        }

        if (!memberSaveRequestDto.getPassword().equals(memberSaveRequestDto.getPasswordCheck())) {
            errors.rejectValue("passwordCheck", "invalid.passwordCheck", "비밀 번호가 다릅니다.");
        }
    }
}
