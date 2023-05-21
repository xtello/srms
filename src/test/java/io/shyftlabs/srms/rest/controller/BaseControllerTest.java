package io.shyftlabs.srms.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class BaseControllerTest {

	@Test
	public void testGetExpectedToken() throws Exception {
		Field isInitializedField = BaseController.class.getDeclaredField("initialized");
		isInitializedField.setAccessible(true);
		Field expectedTokenField = BaseController.class.getDeclaredField("expectedToken");
		expectedTokenField.setAccessible(true);
		Method getExpectedTokenMethod = BaseController.class.getDeclaredMethod("getExpectedToken");
		getExpectedTokenMethod.setAccessible(true);

		CourseController courseController = new CourseController();
		String expectedToken = (String) getExpectedTokenMethod.invoke(courseController);
		assertEquals(System.getenv("SRMS_TOKEN"), expectedToken);

		// Second try (should now be cached)
		expectedToken = (String) getExpectedTokenMethod.invoke(courseController);
		assertEquals(System.getenv("SRMS_TOKEN"), expectedToken);
	}
}
