package io.shyftlabs.srms.rest.resource.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class StudentConverterTest {

	@Test
	public void checkHandlesNull() {
		StudentConverter converter = new StudentConverter();
		assertNull(converter.modelToResource(null));
		assertNull(converter.requestToModel(null));
	}

}
