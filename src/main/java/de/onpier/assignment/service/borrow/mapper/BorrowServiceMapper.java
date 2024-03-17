package de.onpier.assignment.service.borrow.mapper;

import java.time.LocalDate;

import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.util.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BorrowServiceMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", source = "user")
	@Mapping(target = "book", source = "book")
	@Mapping(target = "borrowedFrom", source = "borrowerFrom", qualifiedByName = "localDateToLong")
	@Mapping(target = "borrowedTo", source = "borrowerTo", qualifiedByName = "localDateToLong")
	Borrow toBorrow(User user, Book book, LocalDate borrowerFrom, LocalDate borrowerTo);

	@Named("localDateToLong")
	default Long localDateToLong(LocalDate localDate) {
		return Utils.localDateToLong(localDate);
	}

}
