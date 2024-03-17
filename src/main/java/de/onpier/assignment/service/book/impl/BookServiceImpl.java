package de.onpier.assignment.service.book.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import de.onpier.assignment.dto.book.BookDto;
import de.onpier.assignment.exception.BookNotFoundException;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.book.dao.BookRepository;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.service.book.BookService;
import de.onpier.assignment.service.book.mapper.BookServiceMapper;
import de.onpier.assignment.service.user.UserService;
import de.onpier.assignment.util.Utils;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	private final BookServiceMapper bookServiceMapper;

	private final UserService userService;

	private final BorrowRepository borrowRepository;

	@Override
	public void saveBook(BookDto bookDto) {
		try {
			log.info("gonna save book named with:{}", bookDto.getTitle());
			bookRepository.save(bookServiceMapper.toBook(bookDto));
		} catch (Exception exception) {
			log.warn("exception at saving this book:{}", bookDto);
			log.warn(exception.getMessage());
		}
	}

	@Override
	public Book findByTitle(String bookTitle) throws BookNotFoundException {
		return bookRepository.findByTitle(bookTitle).orElseThrow(() -> new BookNotFoundException("book not found!!!"));
	}

	@Override
	public List<Book> getBooksBorrowedByUserInDateRange(String userId, String startDate, String endDate) throws UserNotExistException {
		if (startDate.isBlank() || endDate.isBlank()) {
			return Collections.emptyList();
		}
		userService.getUserById(userId);

		Long startDateLong = Utils.localDateToLong(Utils.toLocalDate(startDate));
		Long toDateLong = Utils.localDateToLong(Utils.toLocalDate(endDate));

		return borrowRepository.findBooksBorrowedByUserInDateRange(userId, startDateLong, toDateLong);
	}

	@Override
	public List<Book> getAvailableBooks() {
		return bookRepository.findAvailableBooks(Utils.localDateToLong(LocalDate.now()));
	}
}
