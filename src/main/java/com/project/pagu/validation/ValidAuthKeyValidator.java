package com.project.pagu.validation;

import com.project.pagu.annotation.ValidAuthKey;
import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오후 2:13
 */
@RequiredArgsConstructor
public class ValidAuthKeyValidator implements ConstraintValidator<ValidAuthKey, Object> {


    private String authKeyField;
    private String authKeyInputField;
    private String message;

    @Override
    public void initialize(ValidAuthKey constraintAnnotation) {
        this.authKeyField = constraintAnnotation.authKeyField();
        this.authKeyInputField = constraintAnnotation.authKeyInputField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            String encryptKey = (String) getFieldValue(object, authKeyField);
            String authKey = (String) getFieldValue(object, authKeyInputField);
            if (authKey!=null&&!authKey.equals(encryptKey)) {
                setConstraintValidatorContext(context, "인증 번호가 다릅니다.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void setConstraintValidatorContext(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(authKeyInputField)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}
