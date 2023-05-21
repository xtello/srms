package io.shyftlabs.srms.rest.resource.converter;

import java.util.ArrayList;
import java.util.List;

import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.rest.request.StudentRequest;
import io.shyftlabs.srms.rest.resource.StudentResource;

public class StudentConverter {
	public StudentResource modelToResource(Student student) {
		if (student == null) {
			return null;
		}

		return new StudentResource(student.getId(), student.getFirstName(), student.getFamilyName(), student.getEmail(),
				student.getDateOfBirth());
	}

	public List<StudentResource> modelToResources(List<Student> students) {
		List<StudentResource> resources = new ArrayList<>(students.size());
		students.forEach(c -> resources.add(modelToResource(c)));
		return resources;
	}

	public Student requestToModel(StudentRequest studentReq) {
		if (studentReq == null) {
			return null;
		}

		return new Student(studentReq.getFirstName(), studentReq.getFamilyName(), studentReq.getEmail(), studentReq.getDateOfBirth());
	}

}
