package de.onpier.assignment.dto.book;

import lombok.Data;

@Data
public class BookResponse {

	private String title;

	private String author;

	private String publisher;

	private String genre;
}
