package io.shyftlabs.srms.exceptions;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException(String errorMsg) {
		super(errorMsg);
	}
}
