package io.shyftlabs.srms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.StudentRepository;

@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

	@InjectMocks
	private StudentService studentService;

	@Mock
	private StudentRepository studentRepository;


	@Test
	public void createStudentOk() {
		Student student = new Student(1L, "fn", "ln", "email", new GregorianCalendar(2000, 0, 1).getTime());
		when(studentRepository.existsByEmail(student.getEmail())).thenReturn(Boolean.FALSE);
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

		Student insertedStudent = studentService.createStudent(student);
		verify(studentRepository, Mockito.times(1)).save(student);
		assertEquals(student.getId(), insertedStudent.getId());
		assertEquals(student.getFirstName(), insertedStudent.getFirstName());
	}

	@Test
	public void createStudentFailsForDuplicateMail() {
		Student student = new Student(1L, "fn", "ln", "email", new GregorianCalendar(2000, 0, 1).getTime());
		when(studentRepository.existsByEmail(student.getEmail())).thenReturn(Boolean.TRUE);
		assertThrows(DuplicateEntityException.class, () -> studentService.createStudent(student),
				"Student with email \"" + student.getEmail() + "\" already exists");
	}

	@Test
	public void deleteStudentOk() {
		Student student = new Student(1L, "fn", "ln", "email", new GregorianCalendar(2000, 0, 1).getTime());
		when(studentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(student));
		studentService.deleteStudent(1L); // no exception thrown
		verify(studentRepository, Mockito.times(1)).delete(student);
	}

	@Test
	public void deleteStudentFailsCourseDoesNotExist() {
		when(studentRepository.findById(1L)).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.deleteStudent(1L), "No student with ID 1");
	}

}
