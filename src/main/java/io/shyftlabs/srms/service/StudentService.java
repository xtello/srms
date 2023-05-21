package io.shyftlabs.srms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	private Logger logger = LoggerFactory.getLogger(StudentService.class);


	@Transactional(readOnly = true)
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Transactional(readOnly = false)
	public Student createStudent(Student student) {
		String studentEmail = student.getEmail();
		if (studentRepository.existsByEmail(studentEmail)) {
			throw new DuplicateEntityException("Student with email \"" + studentEmail + "\" already exists");
		}

		logger.info("Creating student \"{}\" in DB...", studentEmail);
		student = studentRepository.save(student);
		logger.info("Student created in DB with id {}", student.getId());
		return student;
	}

	@Transactional(readOnly = false)
	public void deleteStudent(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new EntityNotFoundException("No student with ID " + studentId));

		logger.info("Deleting student {}", studentId);
		studentRepository.delete(student);
		logger.info("Student {} deleted", studentId);
	}
}
