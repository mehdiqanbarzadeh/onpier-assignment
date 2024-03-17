package de.onpier.assignment.api.userController;

import java.time.LocalDate;
import java.util.List;

import de.onpier.assignment.AbstractBaseIT;
import de.onpier.assignment.dto.user.UserResponse;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.book.Genre;
import de.onpier.assignment.model.book.dao.BookRepository;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.Gender;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.model.user.dao.UserRepository;
import de.onpier.assignment.util.TestUtils;
import de.onpier.assignment.util.Utils;
import org.assertj.core.api.Assertions;
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

public class UserControllerIT extends AbstractBaseIT {


	private static final String baseUri = "/api/onpier/assignment/users";

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


	@Test
	@DisplayName("Get active users - successful - return active users")
	void getActiveUsers_successful_returning_some_users() {

		saveValidDataInDb();
		String url = TestUtils.baseUrl(port) + baseUri + "/actives";

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isNotZero();

		List<User> users = borrowRepository.findAll().stream().map(Borrow::getUser).toList();
		Assertions.assertThat(users.size()).isEqualTo(response.getBody().size());
	}

	@Test
	@DisplayName("Get active users - successful - return nothing")
	void getActiveUsers_successful_returning_nothing() {

		borrowRepository.deleteAll();

		String url = TestUtils.baseUrl(port) + baseUri + "/actives";

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isZero();

		List<User> users = borrowRepository.findAll().stream().map(Borrow::getUser).toList();
		Assertions.assertThat(users.size()).isEqualTo(response.getBody().size());
	}


	@Test
	@DisplayName("Get non-terminated users who have no current borrows - successful - return some users")
	void nonTerminatedUsersWithNoCurrentBorrows_successful_returning_some_users() {
		saveValidDataInDb();
		borrowRepository.deleteAll();
		String url = TestUtils.baseUrl(port) + baseUri + "/nonTerminate/withoutBorrows";

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isNotZero();

		List<User> users = userRepository.findNonTerminatedUsersWithNoCurrentBorrows(Utils.localDateToLong(LocalDate.now()));
		Assertions.assertThat(users.size()).isEqualTo(response.getBody().size());
	}

	@Test
	@DisplayName("Get non-terminated users who have no current borrows - successful - return nothing")
	void nonTerminatedUsersWithNoCurrentBorrows_successful_returning_nothing() {

		borrowRepository.deleteAll();
		userRepository.deleteAll();

		String url = TestUtils.baseUrl(port) + baseUri + "/nonTerminate/withoutBorrows";

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isZero();

		List<User> users = userRepository.findNonTerminatedUsersWithNoCurrentBorrows(Utils.localDateToLong(LocalDate.now()));
		Assertions.assertThat(users.size()).isEqualTo(response.getBody().size());
	}


	@Test
	@DisplayName("Get users who borrowed books on a specific date - successful - with valid date return some users")
	void getUsersWhoBorrowedBooksOnDate_successful_with_valid_date_return_some_user() {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed")
				.queryParam("date", "06/27/2009");


		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
		Assertions.assertThat(response.getBody().get(0).getFirstName()).isEqualTo("Oliver");
		Assertions.assertThat(response.getBody().get(0).getName()).isEqualTo("Odum");
		Assertions.assertThat(response.getBody().get(0).getGender()).isEqualTo("MALE");
		Assertions.assertThat(response.getBody().get(0).getMemberFrom()).isEqualTo("01/11/1970");
		Assertions.assertThat(response.getBody().get(0).getMemberTill()).isEqualTo("01/19/1970");


		List<User> users = borrowRepository.findUsersWhoBorrowedBooksOnDate(Utils.localDateToLong(Utils.toLocalDate("06/27/2009")));
		Assertions.assertThat(users.size()).isEqualTo(response.getBody().size());
	}


	@Test
	@DisplayName("Get users who borrowed books on a specific date - successful - with invalid date return some exception")
	void getUsersWhoBorrowedBooksOnDate_successful_with_invalid_date_return_some_exception() {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed")
				.queryParam("date", "100/100/2009");


		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<?> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				Object.class);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Get users who borrowed books on a specific date - successful - with invalid date return nothing")
	void getUsersWhoBorrowedBooksOnDate_successful_with_valid_date_return_nothing() {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TestUtils.baseUrl(port) + baseUri + "/borrowed")
				.queryParam("date", "10/10/2009");

		HttpHeaders httpHeaders = TestUtils.getDefaultHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				new ParameterizedTypeReference<>() {});

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody().size()).isEqualTo(0);
	}


	void saveValidDataInDb() {
		Book savedBook = bookRepository.save(getValidBook());
		User savedUser = userRepository.save(getValidUser());
		borrowRepository.save(getValidBorrow(savedUser, savedBook));
	}

	User getValidUser() {
		User user = new User();
		user.setUserId("test123");
		user.setFirstName("testFirstName");
		user.setName("testName");
		user.setGender(Gender.MALE);
		user.setMemberSince(Utils.localDateToLong(LocalDate.now()));
		user.setMemberTill(Utils.localDateToLong(LocalDate.now().plusMonths(1)));
		return user;
	}

	Book getValidBook() {
		Book book = new Book();
		book.setPublisher("testPublisher");
		book.setAuthor("testAuthor");
		book.setTitle("testTitle");
		book.setGenre(Genre.COMPUTER_SCIENCE);
		return book;
	}

	Borrow getValidBorrow(User user, Book book) {
		Borrow borrow = new Borrow();
		borrow.setUser(user);
		borrow.setBook(book);
		borrow.setBorrowedFrom(Utils.localDateToLong(LocalDate.now()));
		borrow.setBorrowedTo(Utils.localDateToLong(LocalDate.now().plusMonths(4)));
		return borrow;
	}

}
