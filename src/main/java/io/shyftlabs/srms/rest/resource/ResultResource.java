package io.shyftlabs.srms.rest.resource;

import io.shyftlabs.srms.domain.Score;

public class ResultResource {

	private Long id;

	private Score score;

	private CourseResource course;

	private StudentResource student;


	public ResultResource() {
		// Empty default constructor
	}

	public ResultResource(Long id, Score score, CourseResource course, StudentResource student) {
		this.id = id;
		this.score = score;
		this.course = course;
		this.student = student;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public CourseResource getCourse() {
		return course;
	}

	public void setCourse(CourseResource course) {
		this.course = course;
	}

	public StudentResource getStudent() {
		return student;
	}

	public void setStudent(StudentResource student) {
		this.student = student;
	}
}
