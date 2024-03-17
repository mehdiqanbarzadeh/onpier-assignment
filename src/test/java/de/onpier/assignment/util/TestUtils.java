package de.onpier.assignment.util;

import java.time.LocalDate;

import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.book.Genre;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.user.Gender;
import de.onpier.assignment.model.user.User;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TestUtils {

	public static String baseUrl(int port) {
		return "http://localhost:" + port;
	}

	public static HttpHeaders getDefaultHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	public static User getValidUser(String userId) {
		User user = new User();
		user.setUserId(userId);
		user.setMemberTill(Utils.localDateToLong(LocalDate.now().plusMonths(5)));
		user.setMemberSince(Utils.localDateToLong(LocalDate.now()));
		user.setName("test");
		user.setGender(Gender.FEMALE);
		user.setFirstName("test");
		return user;
	}


	public static Book getValidBook() {
		Book book = new Book();
		book.setGenre(Genre.COMPUTER_SCIENCE);
		book.setPublisher("testPub");
		book.setTitle("test");
		book.setAuthor("testAuthor");
		return book;
	}

	public static Borrow getValidBorrow(User user, Book book, String from, String to) {
		Borrow borrow = new Borrow();
		borrow.setUser(user);
		borrow.setBook(book);
		borrow.setBorrowedFrom(Utils.localDateToLong(Utils.toLocalDate(from)));
		borrow.setBorrowedTo(Utils.localDateToLong(Utils.toLocalDate(to)));
		return borrow;
	}
}
