package com.project.pagu.validation;

import com.project.pagu.annotation.FieldsValueMatch;
import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 2:45
 */
public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;
    private String message;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Object baseFieldValue = getFieldValue(object, field);
            Object matchFieldValue = getFieldValue(object, fieldMatch);
            valid = baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
            if (!valid) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldMatch)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
            return valid;
        } catch (Exception e) {
            // log error
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }

}
