package io.shyftlabs.srms.rest.resource;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StudentResource {

	private Long id;

	private String firstName;

	private String familyName;

	private String email;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date dateOfBirth;


	public StudentResource() {
		// Empty default constructor
	}

	public StudentResource(Long id, String firstName, String familyName, String email, Date dateOfBirth) {
		this.id = id;
		this.firstName = firstName;
		this.familyName = familyName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
