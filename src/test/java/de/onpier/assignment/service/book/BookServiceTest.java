package de.onpier.assignment.service.book;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.onpier.assignment.dto.book.BookDto;
import de.onpier.assignment.exception.BookNotFoundException;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.book.dao.BookRepository;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.service.book.impl.BookServiceImpl;
import de.onpier.assignment.service.book.mapper.BookServiceMapper;
import de.onpier.assignment.service.user.UserService;
import de.onpier.assignment.util.TestUtils;
import de.onpier.assignment.util.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookServiceMapper bookServiceMapper;

	@Mock
	private UserService userService;

	@Mock
	private BorrowRepository borrowRepository;

	@InjectMocks
	private BookServiceImpl bookService;


	@Test
	public void whenSaveBook_thenBookIsSaved() {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("Test Book");
		Book book = new Book();
		when(bookServiceMapper.toBook(bookDto)).thenReturn(book);

		bookService.saveBook(bookDto);

		verify(bookRepository).save(book);
	}

	@Test
	public void whenSaveBook_thenSucceeds() {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("Test Book");
		Book book = new Book();
		when(bookServiceMapper.toBook(bookDto)).thenReturn(book);

		bookService.saveBook(bookDto);

		verify(bookRepository, times(1)).save(book);
	}

	@Test
	public void whenFindByTitleAndBookExists_thenReturnsBook() {
		String bookTitle = "testTitle";
		Book expectedBook = new Book();
		when(bookRepository.findByTitle(bookTitle)).thenReturn(Optional.of(expectedBook));

		Book result = bookService.findByTitle(bookTitle);

		assertNotNull(result);
		assertEquals(expectedBook, result);
	}

	@Test
	public void whenFindByTitleAndBookDoesNotExist_thenThrowsBookNotFoundException() {
		String bookTitle = "Nonexistent Book";
		when(bookRepository.findByTitle(bookTitle)).thenReturn(Optional.empty());

		assertThrows(BookNotFoundException.class, () -> bookService.findByTitle(bookTitle));
	}

	@Test
	public void whenValidDateRangeAndUserExists_thenReturnsBooks() throws UserNotExistException {
		String userId = "1";
		String startDate = "01/01/2020";
		String endDate = "01/11/2020";
		List<Book> expectedBooks = List.of(new Book());
		when(userService.getUserById(userId)).thenReturn(new User());
		when(borrowRepository.findBooksBorrowedByUserInDateRange(eq(userId), anyLong(), anyLong())).thenReturn(expectedBooks);

		List<Book> result = bookService.getBooksBorrowedByUserInDateRange(userId, startDate, endDate);

		assertFalse(result.isEmpty());
		assertEquals(expectedBooks.size(), result.size());
	}

	@Test
	public void whenInvalidDateRange_thenReturnsEmptyList() throws UserNotExistException {
		String userId = "1";
		String startDate = "";
		String endDate = "";

		List<Book> result = bookService.getBooksBorrowedByUserInDateRange(userId, startDate, endDate);

		assertTrue(result.isEmpty());
	}

	@Test
	public void whenUserDoesNotExist_thenThrowsException() {
		String userId = "nonexistent";
		when(userService.getUserById(userId)).thenThrow(new UserNotExistException("User does not exist"));

		assertThrows(UserNotExistException.class, () -> bookService.getBooksBorrowedByUserInDateRange(userId, "01/01/2020", "01/11/2020"));
	}

	@Test
	public void whenNoBooksBorrowedInDateRange_thenReturnsEmptyList() throws UserNotExistException {
		String userId = "1";
		String startDate = "01/01/2020";
		String endDate = "01/01/2021";
		when(userService.getUserById(userId)).thenReturn(new User());
		when(borrowRepository.findBooksBorrowedByUserInDateRange(eq(userId), anyLong(), anyLong())).thenReturn(Collections.emptyList());

		List<Book> result = bookService.getBooksBorrowedByUserInDateRange(userId, startDate, endDate);

		assertTrue(result.isEmpty());
	}

	@Test
	public void whenAvailableBooksExist_thenReturnsListOfBooks() {
		List<Book> expectedBooks = List.of(new Book());
		when(bookRepository.findAvailableBooks(Utils.localDateToLong(LocalDate.now()))).thenReturn(expectedBooks);

		List<Book> result = bookService.getAvailableBooks();

		assertFalse(result.isEmpty());
		assertEquals(expectedBooks.size(), result.size());
		assertEquals(expectedBooks, result);
	}

	@Test
	public void whenNoAvailableBooks_thenReturnsEmptyList() {
		when(bookRepository.findAvailableBooks(Utils.localDateToLong(LocalDate.now()))).thenReturn(List.of());

		List<Book> result = bookService.getAvailableBooks();

		assertTrue(result.isEmpty());
	}
}
