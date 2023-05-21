package io.shyftlabs.srms.rest.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.shyftlabs.srms.validators.ValidStudentDateOfBirth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentRequest {

	@NotBlank(message = "First name cannot be blank")
	@Size(max = 50, message = "First name cannot be longer than 50 characters")
	private String firstName;

	@NotBlank(message = "Family name cannot be blank")
	@Size(max = 50, message = "Family name cannot be longer than 50 characters")
	private String familyName;

	@Size(max = 100, message = "Email cannot be longer than 100 characters")
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email is not valid")
	private String email;

	@ValidStudentDateOfBirth(message = "Student must be at least 10 years old")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone = "America/Toronto")
	private Date dateOfBirth;


	public StudentRequest() {
		// Empty default constructor

	}

	public StudentRequest(String firstName, String familyName, String email, Date dateOfBirth) {
		this.firstName = firstName;
		this.familyName = familyName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
