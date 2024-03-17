package de.onpier.assignment.model.user;

import java.util.stream.Stream;

public enum Gender {

	MALE(0), FEMALE(1);

	private final int value;

	Gender(int value) {
		this.value = value;
	}

	public static Gender of(String value) {
		switch (value) {
			case "m":
				return Gender.MALE;
			case "f":
				return Gender.FEMALE;
			default:
				throw new IllegalStateException("invalid gender");
		}
	}

	public static Gender of(int value) {
		return Stream.of(Gender.values()).filter(gender -> gender.value == value).findFirst()
				.orElseThrow(() -> new IllegalStateException("undefined value found for gender " + value));
	}

	public int getValue() {
		return value;
	}
}
