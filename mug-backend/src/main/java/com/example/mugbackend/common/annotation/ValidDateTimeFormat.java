package com.example.mugbackend.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.mugbackend.common.validator.DateTimeFormatValidator;

@Constraint(validatedBy = DateTimeFormatValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateTimeFormat {
	String message() default "Invalid date-time format. Expected format is yyyy-MM-dd'T'HH:mm:ss";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
