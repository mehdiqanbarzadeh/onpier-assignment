package de.onpier.assignment.service.book;

import java.util.List;

import de.onpier.assignment.dto.book.BookDto;
import de.onpier.assignment.model.book.Book;
import javassist.NotFoundException;

public interface BookService {
	void saveBook(BookDto bookDto);

	Book findByTitle(String bookTitle) throws NotFoundException;

	List<Book> getBooksBorrowedByUserInDateRange(String userId, String startDate, String endDate) throws NotFoundException;

	List<Book> getAvailableBooks();
}
