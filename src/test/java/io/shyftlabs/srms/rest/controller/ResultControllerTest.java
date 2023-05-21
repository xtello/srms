package io.shyftlabs.srms.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.domain.Result;
import io.shyftlabs.srms.domain.Score;
import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.rest.request.ResultRequest;
import io.shyftlabs.srms.rest.request.ResultRequest.CourseIdObject;
import io.shyftlabs.srms.rest.request.ResultRequest.StudentIdObject;
import io.shyftlabs.srms.service.ResultService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ResultController.class)
public class ResultControllerTest extends AbstractBaseController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ResultController resultController;

	@MockBean
	private ResultService resultService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String RESULTS_ENDPOINT = "/results";


	@Override
	protected BaseController getController() {
		return resultController;
	}

	@Test
	public void getAllResults() throws Exception {
		Student student = new Student("f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course1 = new Course(1L, "Course 1");
		Course course2 = new Course(2L, "Course 2");
		Result result1 = new Result(1L, Score.A, student, course1);
		Result result2 = new Result(1L, Score.A, student, course2);
		List<Result> results = new ArrayList<>();
		results.add(result1);
		results.add(result2);

		Mockito.when(resultService.getAllResults()).thenReturn(results);

		ResultActions response = mockMvc.perform(get(RESULTS_ENDPOINT));
		response.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", is(results.size()))) //
				.andExpect(jsonPath("$[0].id", is(results.get(0).getId().intValue()))) //
				.andExpect(jsonPath("$[0].score", is(results.get(0).getScore().toString()))) //
				.andExpect(jsonPath("$[0].course.name", is(results.get(0).getCourse().getName()))) //
				.andExpect(jsonPath("$[0].student.firstName", is(results.get(0).getStudent().getFirstName()))) //
				.andExpect(jsonPath("$[1].id", is(results.get(1).getId().intValue()))) //
				.andExpect(jsonPath("$[1].score", is(results.get(1).getScore().toString()))) //
				.andExpect(jsonPath("$[1].course.name", is(results.get(1).getCourse().getName()))) //
				.andExpect(jsonPath("$[1].student.firstName", is(results.get(1).getStudent().getFirstName()))) //
		;
	}

	@Test
	public void createResultOk() throws Exception {
		ResultRequest resultRequest = new ResultRequest(new StudentIdObject(1L), new CourseIdObject(1L), Score.A.toString());
		Student student = new Student("f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course = new Course(1L, "Course 1");
		Result result = new Result(1L, Score.A, student, course);

		Mockito.when(resultService.createResult(Mockito.any(Result.class))).thenReturn(result);

		ResultActions response = mockMvc.perform(post(RESULTS_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(objectMapper.writeValueAsString(resultRequest))) //
		;
		response.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.id", is(result.getId().intValue()))) //
				.andExpect(jsonPath("$.score", is(result.getScoreAsString()))) //
				.andExpect(jsonPath("$.course.name", is(course.getName()))) //
				.andExpect(jsonPath("$.student.firstName", is(student.getFirstName()))) //
		;
	}

	@Test
	public void createResultBadRequestInvalidScore() throws Exception {
		ResultRequest resultRequest = new ResultRequest(new StudentIdObject(1L), new CourseIdObject(1L), "invalid-score");

		ResultActions response = mockMvc.perform(post(RESULTS_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(objectMapper.writeValueAsString(resultRequest))) //
		;
		response.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.errorMessage", is("Invalid specified data"))) //
				.andExpect(jsonPath("$.validationErrors[0].field", is("score"))) //
				.andExpect(jsonPath("$.validationErrors[0].message", is("Invalid value"))) //
		;
	}

}