package io.shyftlabs.srms.rest.resource.converter;

import java.util.ArrayList;
import java.util.List;

import io.shyftlabs.srms.domain.Course;
import io.shyftlabs.srms.domain.Result;
import io.shyftlabs.srms.domain.Score;
import io.shyftlabs.srms.domain.Student;
import io.shyftlabs.srms.rest.request.ResultRequest;
import io.shyftlabs.srms.rest.resource.CourseResource;
import io.shyftlabs.srms.rest.resource.ResultResource;
import io.shyftlabs.srms.rest.resource.StudentResource;

public class ResultConverter {
	private CourseConverter courseConverter = new CourseConverter();

	private StudentConverter studentConverter = new StudentConverter();


	public ResultResource modelToResource(Result result) {
		if (result == null) {
			return null;
		}

		CourseResource courseResource = courseConverter.modelToResource(result.getCourse());
		StudentResource studentResource = studentConverter.modelToResource(result.getStudent());
		return new ResultResource(result.getId(), result.getScore(), courseResource, studentResource);
	}

	public List<ResultResource> modelToResources(List<Result> results) {
		List<ResultResource> resources = new ArrayList<>(results.size());
		results.forEach(c -> resources.add(modelToResource(c)));
		return resources;
	}

	public Result requestToModel(ResultRequest resultReq) {
		if (resultReq == null) {
			return null;
		}

		Student student = new Student();
		student.setId(resultReq.getStudent().getId());

		Course course = new Course();
		course.setId(resultReq.getCourse().getId());

		Score score = Score.fromString(resultReq.getScore());

		return new Result(null, score, student, course);
	}

}
