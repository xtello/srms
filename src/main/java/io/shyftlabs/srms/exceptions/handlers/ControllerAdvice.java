package io.shyftlabs.srms.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.shyftlabs.srms.exceptions.DuplicateEntityException;
import io.shyftlabs.srms.exceptions.EntityNotFoundException;
import io.shyftlabs.srms.exceptions.UnauthorizedException;
import io.shyftlabs.srms.responses.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

	private Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);


	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
		logger.warn("Entity not found: {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
		logger.warn("Unauthorized: {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	// Triggered when a "@Valid" fails
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		logger.warn("Request validation error: {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse("Invalid specified data");
		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(InvalidFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
		logger.warn("Request validation error: {}", exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(DuplicateEntityException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEntityExceptions(DuplicateEntityException exception) {
		logger.warn("Bad query: {}", exception);
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUncaughtExceptions(Exception exception) {
		if (exception.getCause() instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) exception.getCause());
		}
		logger.error("Unknown error occurred", exception);
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

	}
}
