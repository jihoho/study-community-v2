package com.project.pagu.validation;

import com.project.pagu.annotation.UniqueEmail;
import com.project.pagu.domain.member.MemberId;
import com.project.pagu.domain.member.MemberType;
import com.project.pagu.service.members.MembersService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 2:27
 */
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {
    private final MembersService membersService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !membersService.existsById(new MemberId(email, MemberType.NORMAL));
    }
}
