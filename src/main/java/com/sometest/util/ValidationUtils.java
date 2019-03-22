package com.sometest.util;

import com.sometest.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtils {

	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static <T> void validate(T o) {

		if (o == null) {
			throw new ValidationException("Object cannot be null");
		}

		Set<ConstraintViolation<T>> validate = validator.validate(o);

		if (validate.size() > 0) {
			throw new ValidationException(validate);
		}
	}

	public static void validateNotNull(Object... fields) {
		for (Object field : fields) {
			if (field == null) {
				throw new ValidationException("Request is incorrect");
			}
		}
	}

}
