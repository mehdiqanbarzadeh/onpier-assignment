package de.onpier.assignment.exception;

import java.util.function.Supplier;

public class BookNotFoundException extends RuntimeException implements Supplier {
	public BookNotFoundException(String s) {
		super(s);
	}

	@Override
	public Object get() {
		return this;
	}
}
