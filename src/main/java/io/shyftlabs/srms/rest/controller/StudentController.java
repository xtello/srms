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

import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.rest.request.StudentRequest;
import io.shyftlabs.srms.rest.resource.StudentResource;
import io.shyftlabs.srms.rest.resource.converter.StudentConverter;
import io.shyftlabs.srms.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
@Tag(name = "Students", description = "Registered students")
public class StudentController extends BaseController {

	@Autowired
	private StudentService studentService;

	private StudentConverter studentConverter = new StudentConverter();

	private Logger logger = LoggerFactory.getLogger(StudentController.class);


	@Operation(summary = "Get all students")
	@GetMapping
	public ResponseEntity<List<StudentResource>> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		logger.info("Found {} students in DB", students.size());
		List<StudentResource> resources = studentConverter.modelToResources(students);
		return ResponseEntity.status(HttpStatus.OK).body(resources);
	}

	@Operation(summary = "Create a student")
	@PostMapping
	public ResponseEntity<StudentResource> createStudent(@Valid @RequestBody StudentRequest studentReq) {
		// Check user token (authentication)
		checkToken();
		
		// Create the object in DB and return its resource
		Student student = studentConverter.requestToModel(studentReq);
		student = studentService.createStudent(student);
		StudentResource createdResource = studentConverter.modelToResource(student);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
	}

	@Operation(summary = "Delete a student")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		// Check user token (authentication)
		checkToken();
		
		studentService.deleteStudent(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
