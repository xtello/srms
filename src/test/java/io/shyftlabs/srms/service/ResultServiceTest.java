package io.shyftlabs.srms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.domain.Result;
import io.shyftlabs.srms.domain.Score;
import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.CourseRepository;
import io.shyftlabs.srms.repository.ResultRepository;
import io.shyftlabs.srms.repository.StudentRepository;

@ExtendWith(SpringExtension.class)
public class ResultServiceTest {

	@InjectMocks
	private ResultService resultService;

	@Mock
	private ResultRepository resultRepository;

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private CourseRepository courseRepository;


	@Test
	public void createResultOk() {
		Student student = new Student(1L, "f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course = new Course(1L, "Course 1");
		Result result = new Result(1L, Score.A, student, course);

		when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
		when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
		when(resultRepository.save(result)).thenReturn(result);

		Result insertedResult = resultService.createResult(result);
		verify(resultRepository, Mockito.times(1)).save(result);
		assertEquals(result.getId(), insertedResult.getId());
		assertEquals(result.getCourse().getName(), insertedResult.getCourse().getName());
		assertEquals(result.getStudent().getFirstName(), insertedResult.getStudent().getFirstName());
	}

	@Test
	public void createResultFailsCourseDoesNotExist() {
		Student student = new Student(1L, "f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course = new Course(1L, "Course 1");
		Result result = new Result(1L, Score.A, student, course);
		when(courseRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		when(studentRepository.findById(Mockito.any())).thenReturn(Optional.of(student));

		assertThrows(EntityNotFoundException.class, () -> resultService.createResult(result), "No course with ID 1");
	}

	@Test
	public void createResultFailsStudentDoesNotExist() {
		Student student = new Student(1L, "f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course = new Course(1L, "Course 1");
		Result result = new Result(1L, Score.A, student, course);
		when(courseRepository.findById(Mockito.any())).thenReturn(Optional.of(course));
		when(studentRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> resultService.createResult(result), "No student with ID 1");
	}

	@Test
	public void createResultFailsDuplicateResult() {
		Student student = new Student(1L, "f1", "l1", "e1", new GregorianCalendar(2000, 0, 1).getTime());
		Course course = new Course(1L, "Course 1");
		Result result = new Result(1L, Score.A, student, course);
		when(resultRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(true);
		assertThrows(DuplicateEntityException.class, () -> resultService.createResult(result),
				"Result alredy exists for course 1 and student 1");
	}
}
