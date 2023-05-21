package io.shyftlabs.srms.rest.resource.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class CourseConverterTest {

	@Test
	public void checkHandlesNull() {
		CourseConverter converter = new CourseConverter();
		assertNull(converter.modelToResource(null));
		assertNull(converter.requestToModel(null));
	}

}
