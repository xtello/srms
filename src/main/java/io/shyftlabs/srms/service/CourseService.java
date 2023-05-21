package io.shyftlabs.srms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.repository.CourseRepository;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	private Logger logger = LoggerFactory.getLogger(CourseService.class);


	@Transactional(readOnly = true)
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	@Transactional(readOnly = false)
	public Course createCourse(Course course) {
		String courseName = course.getName();
		if (courseRepository.existsByName(courseName)) {
			throw new DuplicateEntityException("Course with name \"" + courseName + "\" already exists");
		}

		logger.info("Creating course \"{}\" in DB...", courseName);
		course = courseRepository.save(course);
		logger.info("Course created in DB with id {}", course.getId());
		return course;
	}

	@Transactional(readOnly = false)
	public void deleteCourse(Long courseId) {
		Course course = courseRepository.findById(courseId) //
				.orElseThrow(() -> new EntityNotFoundException("No course with ID " + courseId));

		logger.info("Deleting course {}", courseId);
		courseRepository.delete(course);
		logger.info("Deleted course {}", courseId);
	}

}
