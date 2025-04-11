package com.example.mugbackend.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.mugbackend.common.validator.MainTransactionIdValidator;

@Documented
@Constraint(validatedBy = MainTransactionIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMainTransactionId {
	String message() default "mainTransactionId must not be in combiningTransactionIds";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
