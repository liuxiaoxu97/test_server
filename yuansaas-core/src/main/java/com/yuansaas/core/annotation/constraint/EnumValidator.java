package com.yuansaas.core.annotation.constraint;

import com.yuansaas.core.annotation.EnumValidate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 *
 * 枚举校验
 *
 * @author LXZ 2025/11/18 18:16
 */
public class EnumValidator implements ConstraintValidator<EnumValidate,Object> {



    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidate constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            try {
                Enum.valueOf((Class)enumClass, (String)value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(a -> a.name().equals(value.toString()));
    }
}
