package io.shyftlabs.srms.validators;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateOfBirthValidatorTest {
	private static final long ONE_DAY_IN_MILLI = 24 * 3_600_000;
	private static final long ONE_YEAR_IN_MILLI = 365 * ONE_DAY_IN_MILLI;


	@Test
	public void isValidForOldEnoughDate() {
		DateOfBirthValidator validator = new DateOfBirthValidator();
		long now = System.currentTimeMillis();
		long tenYearsAgo = now - (10 * ONE_YEAR_IN_MILLI);
		Date oldEnoughDate = new Date(tenYearsAgo - 3 * ONE_DAY_IN_MILLI);
		assertTrue(validator.isValid(oldEnoughDate, null));
	}

	@Test
	public void isNotValidForTooRecentDate() {
		DateOfBirthValidator validator = new DateOfBirthValidator();
		long now = System.currentTimeMillis();
		long tenYearsAgo = now - (10 * ONE_YEAR_IN_MILLI);
		Date oldEnoughDate = new Date(tenYearsAgo + 3 * ONE_DAY_IN_MILLI);
		assertFalse(validator.isValid(oldEnoughDate, null));
	}
}
