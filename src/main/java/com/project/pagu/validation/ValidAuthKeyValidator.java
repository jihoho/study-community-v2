package com.project.pagu.validation;

import com.project.pagu.annotation.ValidAuthKey;
import com.project.pagu.domain.email.EmailAuthKey;
import com.project.pagu.service.email.EmailAuthKeyService;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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

    private final EmailAuthKeyService emailAuthKeyService;
    private final PasswordEncoder authKeyEncoder;

    private String idField;
    private String authKeyField;
    private String message;

    @Override
    public void initialize(ValidAuthKey constraintAnnotation) {
        this.idField = constraintAnnotation.idField();
        this.authKeyField = constraintAnnotation.authKeyField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            String idFieldValue = (String) getFieldValue(object, idField);
            String authKeyFieldValue = (String) getFieldValue(object, authKeyField);
            Optional<EmailAuthKey> optional = emailAuthKeyService.findById(idFieldValue);
            if (optional.isPresent()) {
                EmailAuthKey emailAuthKey = optional.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime modifiedDate = emailAuthKey.getModifiedDate();
                if (now.isBefore(modifiedDate)) {
                    throw new Exception("AuthMail.modifiedDate invalid Exception");
                }
                if (ChronoUnit.MINUTES.between(modifiedDate, now) > 30) {
                    context.buildConstraintViolationWithTemplate("인증 시간 30분이 초과 되었습니다. 재인증 해주세요.")
                            .addPropertyNode(authKeyField)
                            .addConstraintViolation()
                            .disableDefaultConstraintViolation();
                    return false;
                } else if (!authKeyEncoder.matches(authKeyFieldValue, emailAuthKey.getAuthKey())) {
                    context.buildConstraintViolationWithTemplate("인증 번호가 다릅니다.")
                            .addPropertyNode(authKeyField)
                            .addConstraintViolation()
                            .disableDefaultConstraintViolation();
                    return false;
                }
            } else {
                context.buildConstraintViolationWithTemplate("재인증 해주세요.")
                        .addPropertyNode(authKeyField)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}
