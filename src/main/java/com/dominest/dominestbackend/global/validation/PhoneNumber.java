package com.dominest.dominestbackend.global.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "잘못된 번호입니다. 01x-xxxx-xxxx 형식으로 입력해주세요.";
    Class[] groups() default {};
    Class[] payload() default {};
}
