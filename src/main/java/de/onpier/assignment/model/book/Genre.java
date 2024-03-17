package de.onpier.assignment.model.book;

import java.util.stream.Stream;

public enum Genre {
	ECONOMICS(0),
	HISTORY(1),
	NONFICTION(2),
	FICTION(3),
	MATHEMATICS(4),
	SCIENCE(5),
	DATA_SCIENCE(6),
	COMPUTER_SCIENCE(7),
	SIGNAL_PROCESSING(8),
	PHILOSOPHY(9),
	PSYCHOLOGY(10);

	private final int value;

	Genre(int value) {
		this.value = value;
	}

	public static Genre of(int value) {
		return Stream.of(Genre.values()).filter(genre -> genre.value == value).findFirst()
				.orElseThrow(() -> new IllegalStateException("undefined value found for genre " + value));
	}

	public static Genre of(String value) {
		return Stream.of(Genre.values()).filter(genre -> genre.name().equals(value.toUpperCase())).findFirst()
				.orElseThrow(() -> new IllegalStateException("undefined value found for genre " + value));
	}

	public int getValue() {
		return value;
	}
}
