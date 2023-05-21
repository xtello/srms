package io.shyftlabs.srms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.CourseRepository;

@ExtendWith(SpringExtension.class)
public class CourseServiceTest {

	@InjectMocks
	private CourseService courseService;

	@Mock
	private CourseRepository courseRepository;


	@Test
	public void createCourseOk() {
		Course course = new Course(1L, "Course 1");
		when(courseRepository.existsByName(Mockito.any())).thenReturn(Boolean.FALSE);
		when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);

		Course insertedCourse = courseService.createCourse(course);
		verify(courseRepository, Mockito.times(1)).save(course);
		assertEquals(course.getId(), insertedCourse.getId());
		assertEquals(course.getName(), insertedCourse.getName());
	}

	@Test
	public void createCourseFailsForDuplicateCourse() {
		Course course = new Course(1L, "Course 1");
		when(courseRepository.existsByName(Mockito.any())).thenReturn(Boolean.TRUE);
		assertThrows(DuplicateEntityException.class, () -> courseService.createCourse(course),
				"Course with name \"" + course.getName() + "\" already exists");
	}

	@Test
	public void deleteCourseOk() {
		Course course = new Course(1L, "Course 1");
		when(courseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(course));
		courseService.deleteCourse(1L); // no exception thrown
		verify(courseRepository, Mockito.times(1)).delete(course);
	}

	@Test
	public void deleteCourseFailsCourseDoesNotExist() {
		when(courseRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> courseService.deleteCourse(1L), "No course with ID 1");
	}
}
