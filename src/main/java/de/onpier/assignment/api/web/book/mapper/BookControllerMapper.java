package de.onpier.assignment.api.web.book.mapper;

import java.util.List;

import de.onpier.assignment.dto.book.BookResponse;
import de.onpier.assignment.model.book.Book;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookControllerMapper {

	@IterableMapping(qualifiedByName = "toBookResponse")
	List<BookResponse> ToBookResponses(List<Book> books);

	@Named("toBookResponse")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "publisher", target = "publisher")
	@Mapping(source = "genre", target = "genre")
	@Mapping(source = "author", target = "author")
	BookResponse toBookResponse(Book book);
}
