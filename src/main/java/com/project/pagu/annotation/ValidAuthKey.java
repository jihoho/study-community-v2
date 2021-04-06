package com.project.pagu.annotation;

import com.project.pagu.validation.ValidAuthKeyValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오후 2:05
 */

@Constraint(validatedBy = ValidAuthKeyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthKey {

    String message() default "invalid auth key!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String authKeyField();

    String authKeyInputField();
}
