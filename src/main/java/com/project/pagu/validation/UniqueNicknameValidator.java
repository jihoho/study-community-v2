package com.project.pagu.validation;

import com.project.pagu.annotation.UniqueNickname;
import com.project.pagu.service.members.MembersService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 2:09
 */
@RequiredArgsConstructor
public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

    private final MembersService membersService;

    @Override
    public void initialize(UniqueNickname constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        return !membersService.existsByNickname(nickname);
    }
}
