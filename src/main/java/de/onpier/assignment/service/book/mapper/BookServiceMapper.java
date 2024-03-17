package de.onpier.assignment.service.book.mapper;

import de.onpier.assignment.dto.book.BookDto;
import de.onpier.assignment.model.book.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookServiceMapper {
	Book toBook(BookDto bookDto);
}
