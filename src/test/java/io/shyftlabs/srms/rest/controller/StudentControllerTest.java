package io.shyftlabs.srms.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.rest.request.StudentRequest;
import io.shyftlabs.srms.rest.resource.converter.StudentConverter;
import io.shyftlabs.srms.service.StudentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StudentController.class)
public class StudentControllerTest extends AbstractBaseController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentController studentController;

	@MockBean
	private StudentService studentService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String STUDENTS_ENDPOINT = "/students";


	@Override
	protected BaseController getController() {
		return studentController;
	}

	@Test
	public void getAllStudents() throws Exception {
		Student student1 = new Student("f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Student student2 = new Student("f2", "l2", "e2", new GregorianCalendar(2000, 0, 2).getTime());
		List<Student> students = Arrays.asList(student1, student2);

		Mockito.when(studentService.getAllStudents()).thenReturn(students);

		ResultActions response = mockMvc.perform(get(STUDENTS_ENDPOINT));
		response.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.size()", is(students.size()))) //
				.andExpect(jsonPath("$[0].firstName", is(student1.getFirstName()))) //
				.andExpect(jsonPath("$[0].familyName", is(student1.getFamilyName()))) //
				.andExpect(jsonPath("$[0].email", is(student1.getEmail()))) //
				.andExpect(jsonPath("$[0].dateOfBirth", is("01/01/2000"))) //
				.andExpect(jsonPath("$[1].firstName", is(student2.getFirstName()))) //
				.andExpect(jsonPath("$[1].familyName", is(student2.getFamilyName()))) //
				.andExpect(jsonPath("$[1].email", is(student2.getEmail()))) //
				.andExpect(jsonPath("$[1].dateOfBirth", is("01/02/2000"))) //
		;
	}

	@Test
	public void createStudentOk() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24 * 368 * 10);
		Date moreThan10YearsAgo = calendar.getTime();
		StudentRequest studentRequest = new StudentRequest("fn", "ln", "email@shyftlabs.io", moreThan10YearsAgo);
		Student student = new StudentConverter().requestToModel(studentRequest);
		student.setId(1L);

		Mockito.when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(student);

		ResultActions response = mockMvc.perform(post(STUDENTS_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(objectMapper.writeValueAsString(studentRequest))) //
		;
		response.andExpect(status().isCreated()) //
				.andExpect(jsonPath("$.id", is(student.getId().intValue()))) //
				.andExpect(jsonPath("$.firstName", is(student.getFirstName()))) //
				.andExpect(jsonPath("$.familyName", is(student.getFamilyName()))) //
				.andExpect(jsonPath("$.email", is(student.getEmail()))) //
		;
	}

	@Test
	public void createStudentBadRequestStudentTooYoung() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24 * 360 * 10);
		Date lessThan10YearsAgo = calendar.getTime();
		StudentRequest studentRequest = new StudentRequest("fn", "ln", "email@shyftlabs.io", lessThan10YearsAgo);
		Student student = new StudentConverter().requestToModel(studentRequest);

		Mockito.when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(student);

		ResultActions response = mockMvc.perform(post(STUDENTS_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(objectMapper.writeValueAsString(studentRequest))) //
		;
		response.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.errorMessage", is("Invalid specified data"))) //
				.andExpect(jsonPath("$.validationErrors[0].field", is("dateOfBirth"))) //
				.andExpect(jsonPath("$.validationErrors[0].message", is("Student must be at least 10 years old"))) //
		;
	}

	@Test
	public void createStudentBadRequestInvalidEmail() throws Exception {
		StudentRequest addStudentRequest = new StudentRequest("fn", "ln", "foo.io", new GregorianCalendar(2000, 0, 1).getTime());

		ResultActions response = mockMvc.perform(post(STUDENTS_ENDPOINT) //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(objectMapper.writeValueAsString(addStudentRequest))) //
		;
		response.andExpect(status().isBadRequest()) //
				.andExpect(jsonPath("$.errorMessage", is("Invalid specified data"))) //
				.andExpect(jsonPath("$.validationErrors[0].field", is("email"))) //
				.andExpect(jsonPath("$.validationErrors[0].message", is("Email is not valid"))) //
		;

	}

	@Test
	public void deleteStudentOk() throws Exception {
		ResultActions response = mockMvc.perform(delete(STUDENTS_ENDPOINT + "/1") //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)) //
		;
		response.andExpect(status().isNoContent());
	}

	@Test
	public void deleteStudentNotFound() throws Exception {
		Mockito.doThrow(new EntityNotFoundException("")).when(studentService).deleteStudent(404L);

		ResultActions response = mockMvc.perform(delete(STUDENTS_ENDPOINT + "/404") //
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN)) //
		;
		response.andExpect(status().isNotFound());
	}
}
