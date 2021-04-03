package com.project.pagu.annotation;

import com.project.pagu.validation.UniqueEmailValidator;
import com.project.pagu.validation.UniqueNicknameValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 2:24
 */
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "이미 존재하는 이메일입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
