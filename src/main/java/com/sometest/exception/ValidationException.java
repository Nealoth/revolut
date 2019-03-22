package com.sometest.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;

@Getter
public class ValidationException extends RuntimeException {

	private final Set<ConstraintViolation> violations;

	public ValidationException(Set violations) {
		this.violations = violations;
	}

	public ValidationException(String message) {
		super(message);
		this.violations = Collections.emptySet();
	}
}
