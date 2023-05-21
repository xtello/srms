package io.shyftlabs.srms.rest.resource.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ResultConverterTest {

	@Test
	public void checkHandlesNull() {
		ResultConverter converter = new ResultConverter();
		assertNull(converter.modelToResource(null));
		assertNull(converter.requestToModel(null));
	}

}
