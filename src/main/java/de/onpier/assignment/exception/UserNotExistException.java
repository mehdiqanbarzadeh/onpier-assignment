package de.onpier.assignment.exception;

import java.util.function.Supplier;

public class UserNotExistException extends RuntimeException implements Supplier {
	public UserNotExistException(String message) {
		super(message);
	}

	@Override
	public Object get() {
		return this;
	}
}
