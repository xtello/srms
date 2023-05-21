package io.shyftlabs.srms.rest.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractBaseController {
	protected final String TEST_TOKEN = "123";


	protected abstract BaseController getController();

	@BeforeAll
	public void initTest() {
		getController().setExpectedToken(TEST_TOKEN);
	}
}
