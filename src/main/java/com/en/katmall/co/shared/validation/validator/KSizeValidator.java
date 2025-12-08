/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Map;

/**
 * Validator for {@link KSize} annotation.
 * Supports String, Collection, Map, and Array types.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KSizeValidator implements ConstraintValidator<KSize, Object> {

    private int min;
    private int max;
    private String field;
    private String messageCode;

    @Override
    public void initialize(KSize annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.field = annotation.field();
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Null is valid (use @KNotBlank for null check)
        if (value == null) {
            return true;
        }

        int size = getSize(value);

        if (size < min) {
            setViolation(context, "k.validation.size.min");
            return false;
        }

        if (size > max) {
            setViolation(context, "k.validation.size.max");
            return false;
        }

        return true;
    }

    private int getSize(Object value) {
        if (value instanceof String) {
            return ((String) value).length();
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).size();
        } else if (value instanceof Map) {
            return ((Map<?, ?>) value).size();
        } else if (value.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(value);
        }
        return 0;
    }

    private void setViolation(ConstraintValidatorContext context, String msgCode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msgCode)
                .addConstraintViolation();
    }
}
