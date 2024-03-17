package de.onpier.assignment.dto.book;

import de.onpier.assignment.model.book.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto {

	private String title;

	private String author;

	private Genre genre;

	private String publisher;
}
