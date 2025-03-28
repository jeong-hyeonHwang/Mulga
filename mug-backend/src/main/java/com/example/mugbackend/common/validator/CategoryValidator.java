package com.example.mugbackend.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.mugbackend.common.annotation.ValidCategory;
import com.example.mugbackend.common.enumeration.CategoryEnum;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		try {
			CategoryEnum category = CategoryEnum.valueOf(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
