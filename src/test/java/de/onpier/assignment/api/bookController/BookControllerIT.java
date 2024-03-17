package de.onpier.assignment.api.bookController;

import java.time.LocalDate;
import java.util.List;

import de.onpier.assignment.AbstractBaseIT;
import de.onpier.assignment.dto.book.BookResponse;
import de.onpier.assignment.dto.user.UserResponse;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.book.dao.BookRepository;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.model.user.dao.UserRepository;
import de.onpier.assignment.util.TestUtils;
import de.onpier.assignment.util.Utils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class BookControllerIT extends AbstractBaseIT {


	private static final String baseUri = "/api/onpier/assignment/books";

	@Autowired
	UserRepository userRepository;

	@Autowired
	BorrowRepository borrowRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@BeforeEach
	void destroy() {
		borrowRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}


	@Test
	@DisplayName("Get books borrowed by a user within a date range - successful")
	void getBooksBorrowedByUserBetweenDates_successful_returning_valid_content() {
		User savedUser = userRepository.save(TestUtils.getValidUser("testUser123"));
		Book savedBook = bookRepository.save(TestUtils.getValidBook());
		borrowRepository.save(TestUtils.getValidBorrow(savedUser,savedBook,"01/01/2020","11/11/2020"));


		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed/by-user")
				.queryParam("userId", savedUser.getUserId()).queryParam("startDate","01/01/2000").queryParam("endDate","11/11/2023");

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<BookResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
		Assertions.assertThat(response.getBody().get(0).getTitle()).isEqualTo(savedBook.getTitle());
		Assertions.assertThat(response.getBody().get(0).getGenre()).isEqualTo(savedBook.getGenre().name());
		Assertions.assertThat(response.getBody().get(0).getAuthor()).isEqualTo(savedBook.getAuthor());
		Assertions.assertThat(response.getBody().get(0).getPublisher()).isEqualTo(savedBook.getPublisher());

		List<Book> books = borrowRepository.findBooksBorrowedByUserInDateRange(savedUser.getUserId(), Utils.localDateToLong(Utils.toLocalDate("01/01/2020")), Utils.localDateToLong(Utils.toLocalDate("11/11/2020")));
		Assertions.assertThat(books.size()).isEqualTo(response.getBody().size());




	}

	@Test
	@DisplayName("Get books borrowed by invalid user within a date range - return some exception")
	void getBooksBorrowedByUserBetweenDates_with_invalid_userId_return_exception() {
		User savedUser = userRepository.save(TestUtils.getValidUser("testUser123"));
		Book savedBook = bookRepository.save(TestUtils.getValidBook());
		borrowRepository.save(TestUtils.getValidBorrow(savedUser,savedBook,"01/01/2020","11/11/2020"));


		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed/by-user")
				.queryParam("userId", "invalid-user-id").queryParam("startDate","01/01/2000").queryParam("endDate","11/11/2023");

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<?> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				Object.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	@DisplayName("Get books borrowed by valid user within a blank date range - return empty list")
	void getBooksBorrowedByUserBetweenDates_with_valid_userId_blank_date_range_return_empty_list() {
		User savedUser = userRepository.save(TestUtils.getValidUser("testUser123"));
		Book savedBook = bookRepository.save(TestUtils.getValidBook());
		borrowRepository.save(TestUtils.getValidBorrow(savedUser,savedBook,"01/01/2020","11/11/2020"));


		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed/by-user")
				.queryParam("userId", savedUser.getUserId()).queryParam("startDate","").queryParam("endDate","");

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<BookResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isZero();
	}


	@Test
	@DisplayName("Returns a list of all books that are currently available for borrowing - successful")
	void getAvailableBooks_successful() {
		Book savedBook = bookRepository.save(TestUtils.getValidBook());

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/available");
		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<BookResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		List<Book> availableBooks = bookRepository.findAvailableBooks(Utils.localDateToLong(LocalDate.now()));

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isEqualTo(availableBooks.size());
	}


	@Test
	@DisplayName("Returns a list of all books that are currently available for borrowing - successful - response list must be empty")
	void getAvailableBooks_successful_there_is_no_available_book_for_now() {
		User savedUser = userRepository.save(TestUtils.getValidUser("testUser123"));
		Book savedBook = bookRepository.save(TestUtils.getValidBook());
		borrowRepository.save(TestUtils.getValidBorrow(savedUser,savedBook,"01/01/2020","11/11/2050"));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/available");
		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<BookResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		List<Book> availableBooks = bookRepository.findAvailableBooks(Utils.localDateToLong(LocalDate.now()));

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isEqualTo(availableBooks.size());
		Assertions.assertThat(response.getBody().size()).isZero();
		Assertions.assertThat(availableBooks.size()).isZero();

	}
}
