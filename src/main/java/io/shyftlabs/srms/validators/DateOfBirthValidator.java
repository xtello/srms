package io.shyftlabs.srms.validators;

import java.util.Calendar;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateOfBirthValidator implements ConstraintValidator<ValidStudentDateOfBirth, Date> {

	@Override
	public boolean isValid(Date dateOfBirth, ConstraintValidatorContext context) {
		// Compute the date "10 years ago" without any leap year days
		Calendar tenYearsAgo = Calendar.getInstance();
		tenYearsAgo.set(Calendar.YEAR, tenYearsAgo.get(Calendar.YEAR) - 10);

		// Set the same time for both the given date and the "10 years ago" date 
		Calendar dateOfBirthCal = Calendar.getInstance();
		dateOfBirthCal.setTime(dateOfBirth);
		dateOfBirthCal.set(Calendar.HOUR_OF_DAY, tenYearsAgo.get(Calendar.HOUR_OF_DAY));
		dateOfBirthCal.set(Calendar.MINUTE, tenYearsAgo.get(Calendar.MINUTE));

		// Given date is valid if it's before the "10 years ago" date
		return dateOfBirthCal.before(tenYearsAgo);
	}
}
