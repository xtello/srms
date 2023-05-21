package io.shyftlabs.srms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.domain.Result;
import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.CourseRepository;
import io.shyftlabs.srms.repository.ResultRepository;
import io.shyftlabs.srms.repository.StudentRepository;

@Service
public class ResultService {

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	private Logger logger = LoggerFactory.getLogger(ResultService.class);


	@Transactional(readOnly = true)
	public List<Result> getAllResults() {
		return resultRepository.findAll();
	}

	@Transactional(readOnly = false)
	public Result createResult(Result result) {
		long courseId = result.getCourse().getId();
		long studentId = result.getStudent().getId();

		if (resultRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
			throw new DuplicateEntityException("Result alredy exists for course " + courseId + " and student " + studentId);
		}

		Course course = courseRepository.findById(courseId) //
				.orElseThrow(() -> new EntityNotFoundException("No course with ID " + courseId));

		Student student = studentRepository.findById(studentId) //
				.orElseThrow(() -> new EntityNotFoundException("No student with ID " + studentId));

		result.setCourse(course);
		result.setStudent(student);

		logger.info("Creating result in DB for student {}, course {}", courseId, studentId);
		result = resultRepository.save(result);
		logger.info("Result creating in DB with id {}", result.getId());
		return result;
	}

}
