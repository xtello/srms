package io.shyftlabs.srms.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.shyftlabs.srms.domain.Score;
import io.shyftlabs.srms.validators.ValueOfEnum;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultRequest {

	@NotNull(message = "'student' not specified")
	private StudentIdObject student;

	@NotNull(message = "'course' not specified")
	private CourseIdObject course;

	@ValueOfEnum(enumClass = Score.class)
	private String score;


	public ResultRequest() {
		// Empty default constructor
	}

	public ResultRequest(StudentIdObject student, CourseIdObject course, String score) {
		this.student = student;
		this.course = course;
		this.score = score;
	}

	public CourseIdObject getCourse() {
		return course;
	}

	public void setCourse(CourseIdObject course) {
		this.course = course;
	}

	public StudentIdObject getStudent() {
		return student;
	}

	public void setStudent(StudentIdObject student) {
		this.student = student;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CourseIdObject {
		@NotNull(message = "Course id not specified")
		private Long id;


		public CourseIdObject() {
			// Empty default constructor
		}

		public CourseIdObject(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StudentIdObject {
		@NotNull(message = "Student id not specified")
		private Long id;


		public StudentIdObject() {
			// Empty default constructor
		}

		public StudentIdObject(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

	}
}
