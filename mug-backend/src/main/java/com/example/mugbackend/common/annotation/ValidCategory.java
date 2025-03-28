package com.example.mugbackend.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.mugbackend.common.validator.CategoryValidator;

@Constraint(validatedBy = CategoryValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {
	String message() default "Invalid category value.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
