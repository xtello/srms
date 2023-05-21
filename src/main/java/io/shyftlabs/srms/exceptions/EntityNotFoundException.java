package io.shyftlabs.srms.exceptions;

public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String errorMsg) {
		super(errorMsg);
	}
}
