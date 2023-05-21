package io.shyftlabs.srms.exceptions;

public class DuplicateEntityException extends RuntimeException {
	public DuplicateEntityException(String errorMsg) {
		super(errorMsg);
	}
}
