package io.shyftlabs.srms.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "results")
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	private Score score; // Persistence of this Enum field is done through its getter/setter annotated @Column

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;


	public Result() {
		// Empty default constructor
	}

	public Result(Long id, Score score, Student student, Course course) {
		this.id = id;
		this.score = score;
		this.student = student;
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "score", nullable = false)
	@Access(AccessType.PROPERTY)
	public String getScoreAsString() {
		return score == null ? null : score.toString();
	}

	@Column(name = "score", nullable = false)
	@Access(AccessType.PROPERTY)
	public void setScoreAsString(String score) {
		this.score = Score.fromString(score);
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
