package io.shyftlabs.srms.rest.resource.converter;

import java.util.ArrayList;
import java.util.List;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.rest.request.CourseRequest;
import io.shyftlabs.srms.rest.resource.CourseResource;

public class CourseConverter {
	public CourseResource modelToResource(Course course) {
		if (course == null) {
			return null;
		}

		return new CourseResource(course.getId(), course.getName());
	}

	public List<CourseResource> modelToResources(List<Course> courses) {
		List<CourseResource> resources = new ArrayList<>(courses.size());
		courses.forEach(c -> resources.add(modelToResource(c)));
		return resources;
	}

	public Course requestToModel(CourseRequest courseReq) {
		if (courseReq == null) {
			return null;
		}

		return new Course(courseReq.getName());
	}

}
