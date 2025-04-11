package com.example.mugbackend.common.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.mugbackend.common.annotation.ValidDateTimeFormat;

public class DateTimeFormatValidator implements ConstraintValidator<ValidDateTimeFormat, String> {
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
		dateFormat.setLenient(false); // Disable lenient parsing (e.g., no accepting "2025-02-30")
		try {
			dateFormat.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
