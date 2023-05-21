package io.shyftlabs.srms.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.rest.request.CourseRequest;
import io.shyftlabs.srms.service.CourseService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CourseController.class)
public class CourseControllerTest extends AbstractBaseController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CourseController courseController;

	@MockBean
	private CourseService courseService;

	private ObjectMapper jsonMapper = new ObjectMapper();

	private static final String COURSES_ENDPOINT = "/courses";


	@Override
	protected BaseController getController() {
		return courseController;
	}

	@Test
	public void getAllCourses() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1L, "Course 1"));
		courses.add(new Course(2L, "Course 2"));

		Mockito.when(courseService.getAllCourses()).thenReturn(courses);

		ResultActions response = mockMvc.perform(get(COURSES_ENDPOINT));
		response //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", is(courses.size()))) //
				.andExpect(jsonPath("$[0].id", is(courses.get(0).getId().intValue()))) //
				.andExpect(jsonPath("$[0].name", is(courses.get(0).getName()))) //
				.andExpect(jsonPath("$[1].id", is(courses.get(1).getId().intValue()))) //
				.andExpect(jsonPath("$[1].name", is(courses.get(1).getName()))) //
		;
	}

	@Test
	public void createCourseOk() throws Exception {
		CourseRequest addCourseRequest = new CourseRequest("Course 1");

		Mockito.when(courseService.createCourse(Mockito.any(Course.class))) //
				.thenReturn(new Course(1L, "Course 1"));

		ResultActions response = mockMvc.perform(post(COURSES_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(jsonMapper.writeValueAsString(addCourseRequest))) //
		;
		response //
				.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.id", is(1))) //
				.andExpect(jsonPath("$.name", is("Course 1"))) //
		;
	}

	@Test
	public void createCourseUnauthorized() throws Exception {
		//  Missing credentials
		mockMvc.perform(post(COURSES_ENDPOINT) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(jsonMapper.writeValueAsString(new CourseRequest("Course 1")))) //
				.andExpect(status().isUnauthorized()) //
		;

		//  Malformed credentials (no "Bearer")
		mockMvc.perform(post(COURSES_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(jsonMapper.writeValueAsString(new CourseRequest("Course 1")))) //
				.andExpect(status().isUnauthorized()) //
		;

		//  Invalid credentials
		mockMvc.perform(post(COURSES_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer bad-token") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(jsonMapper.writeValueAsString(new CourseRequest("Course 1")))) //
				.andExpect(status().isUnauthorized()) //
		;
	}

	@Test
	public void createCourseBadRequest() throws Exception {
		DuplicateEntityException dupException = new DuplicateEntityException("mock message");
		Mockito.doThrow(dupException).when(courseService).createCourse(Mockito.any(Course.class));
		CourseRequest addCourseRequest = new CourseRequest("foo");
		ResultActions response = mockMvc.perform(post(COURSES_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(jsonMapper.writeValueAsString(addCourseRequest))) //
		;
		response.andExpect(status().isConflict()) //
				.andExpect(jsonPath("$.errorMessage", is(dupException.getMessage()))) //
		;
	}

	@Test
	public void deleteCourseOk() throws Exception {
		ResultActions response = mockMvc.perform(delete(COURSES_ENDPOINT + "/1") //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)) //
		;
		response.andExpect(status().isNoContent());
	}

	@Test
	public void deleteCourseNotFound() throws Exception {
		Mockito.doThrow(new EntityNotFoundException("")).when(courseService).deleteCourse(404L);

		ResultActions response = mockMvc.perform(delete(COURSES_ENDPOINT + "/404") //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)) //
		;
		response.andExpect(status().isNotFound());
	}

	@Test
	public void deleteCourseUnauthorized() throws Exception {
		ResultActions response = mockMvc.perform(delete(COURSES_ENDPOINT + "/1"));
		response.andExpect(status().isUnauthorized());
	}

}
