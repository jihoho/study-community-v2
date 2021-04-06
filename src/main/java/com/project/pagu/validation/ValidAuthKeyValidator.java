package com.project.pagu.validation;

import com.project.pagu.annotation.ValidAuthKey;
import com.project.pagu.signup.domain.EmailAuthKey;
import com.project.pagu.signup.service.EmailAuthKeyService;
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
            boolean valid = isValidAuthKey(optional, authKeyFieldValue, context);
            return valid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isValidAuthKey(Optional<EmailAuthKey> optional, String emailToken,
            ConstraintValidatorContext context) throws Exception {
        if (optional.isPresent()) {
            EmailAuthKey emailAuthKey = optional.get();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime modifiedDate = emailAuthKey.getModifiedDate();
            if (now.isBefore(modifiedDate)) {
                throw new Exception("AuthMail.modifiedDate invalid Exception");
            }
            if (ChronoUnit.MINUTES.between(modifiedDate, now) > 30) {
                setConstraintValidatorContext(context, "인증 시간 30분이 초과 되었습니다. 재인증 해주세요.");
                return false;
            } else if (!authKeyEncoder.matches(emailToken, emailAuthKey.getAuthKey())) {
                setConstraintValidatorContext(context, "인증 번호가 다릅니다.");
                return false;
            }
        } else {
            setConstraintValidatorContext(context, "재인증 해주세요.");
            return false;
        }
        return true;
    }

    private void setConstraintValidatorContext(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(authKeyField)
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
