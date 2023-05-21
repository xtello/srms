package io.shyftlabs.srms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentResultManagementApplicationTests {
	@Autowired
	private MockMvc mockMvc;


	@Test
	public void integrationTestSample() throws Exception {

		// This is an example of Integration Test that runs against the entire application stack
		// (i.e. Database/Service/Controller) without any mocks. Here we should probably either use
		// an in-memory DB like H2, or a dedicated real DB (e.g. Postgres DB dedicated to these tests)
		mockMvc.perform(get("/courses")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", not(is(0)))) //
		;

		mockMvc.perform(get("/students")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", not(is(0)))) //
		;

		mockMvc.perform(get("/results")) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", not(is(0)))) //
		;

		// More tests and assertions should be added here for real life applications

	}

}
