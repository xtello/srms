package io.shyftlabs.srms.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseRequest {

	@NotBlank(message = "Course cannot be blank")
	@Size(max = 128, message = "Course name cannot be longer than 128 characters")
	private String name;


	public CourseRequest() {
		// Empty default constructor
	}

	public CourseRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
