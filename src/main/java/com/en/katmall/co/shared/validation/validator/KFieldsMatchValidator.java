/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.validation.validator;

import com.en.katmall.co.shared.validation.annotation.KFieldsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Validator for {@link KFieldsMatch} annotation.
 * Compares two field values for equality.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class KFieldsMatchValidator implements ConstraintValidator<KFieldsMatch, Object> {

    private String field;
    private String fieldMatch;
    private String messageCode;

    @Override
    public void initialize(KFieldsMatch annotation) {
        this.field = annotation.field();
        this.fieldMatch = annotation.fieldMatch();
        this.messageCode = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            BeanWrapper beanWrapper = new BeanWrapperImpl(value);
            Object fieldValue = beanWrapper.getPropertyValue(field);
            Object fieldMatchValue = beanWrapper.getPropertyValue(fieldMatch);

            // Both null is considered valid
            if (fieldValue == null && fieldMatchValue == null) {
                return true;
            }

            // Check equality
            boolean isValid = fieldValue != null && fieldValue.equals(fieldMatchValue);

            if (!isValid) {
                // Disable default constraint violation
                context.disableDefaultConstraintViolation();

                // Add violation to the fieldMatch field (e.g., confirmPassword)
                context.buildConstraintViolationWithTemplate(messageCode)
                        .addPropertyNode(fieldMatch)
                        .addConstraintViolation();
            }

            return isValid;

        } catch (Exception e) {
            // If field access fails, consider invalid
            return false;
        }
    }
}
