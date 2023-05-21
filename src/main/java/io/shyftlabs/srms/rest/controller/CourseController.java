package io.shyftlabs.srms.rest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.rest.request.CourseRequest;
import io.shyftlabs.srms.rest.resource.CourseResource;
import io.shyftlabs.srms.rest.resource.converter.CourseConverter;
import io.shyftlabs.srms.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
@Tag(name = "Courses", description = "Existing courses")
public class CourseController extends BaseController {

	@Autowired
	private CourseService courseService;

	private CourseConverter courseConverter = new CourseConverter();

	private Logger logger = LoggerFactory.getLogger(CourseController.class);


	@Operation(summary = "Get all courses")
	@GetMapping
	public ResponseEntity<List<CourseResource>> getAllCourses() {
		List<Course> courses = courseService.getAllCourses();
		logger.info("Found {} courses in DB", courses.size());
		List<CourseResource> resources = courseConverter.modelToResources(courses);
		return ResponseEntity.status(HttpStatus.OK).body(resources);
	}

	@Operation(summary = "Create a course")
	@PostMapping
	public ResponseEntity<CourseResource> createCourse(@Valid @RequestBody CourseRequest courseReq) {
		// Check user token (authentication)
		checkToken();

		// Create the object in DB and return its resource
		Course course = courseConverter.requestToModel(courseReq);
		course = courseService.createCourse(course);
		CourseResource createdResource = courseConverter.modelToResource(course);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
	}

	@Operation(summary = "Delete a course")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		// Check user token (authentication)
		checkToken();

		courseService.deleteCourse(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
