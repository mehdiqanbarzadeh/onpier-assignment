package de.onpier.assignment.service.user;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.model.user.dao.UserRepository;
import de.onpier.assignment.service.user.impl.UserServiceImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


	@Mock
	private UserRepository userRepository;

	@Mock
	private BorrowRepository borrowRepository;

	@InjectMocks
	private UserServiceImpl userService;


	@Test
	public void whenFindByFullNameWithNulls_thenShouldHandleGracefully() {
		String name = null;
		String firstName = null;

		assertThrows(IllegalArgumentException.class, () -> userService.findByUserFullName(name, firstName));
	}

	@Test
	public void whenFindByFullNameWithEmptyStrings_thenShouldHandleGracefully() {
		String name = "";
		String firstName = "";

		assertThrows(IllegalArgumentException.class, () -> userService.findByUserFullName(name, firstName));
	}

	@Test
	public void whenNotFindByFullNameWithWrongName_thenShouldHandleGracefully() {
		String name = "test";
		String firstName = "test";

		assertThrows(UserNotExistException.class, () -> userService.findByUserFullName(name, firstName));

		verify(userRepository, times(1)).findByNameAndFirstName(name, firstName);
	}


	@Test
	public void whenGetActiveUsers_thenReturnsListOfUsers() {
		User user1 = new User();
		User user2 = new User();
		Borrow borrow1 = new Borrow();
		borrow1.setUser(user1);
		Borrow borrow2 = new Borrow();
		borrow2.setUser(user2);
		when(borrowRepository.findAll()).thenReturn(Arrays.asList(borrow1, borrow2));


		List<User> result = userService.getActiveUsers();


		assertEquals(2, result.size());
		assertTrue(result.containsAll(Arrays.asList(user1, user2)));
	}

	@Test
	public void whenGetActiveUsersAndRepositoryIsEmpty_thenReturnsEmptyList() {
		when(borrowRepository.findAll()).thenReturn(List.of());

		List<User> result = userService.getActiveUsers();

		assertTrue(result.isEmpty());
	}


	@Test
	public void whenNonTerminatedUsersWithNoCurrentBorrows_thenReturnUsers() {
		User user1 = new User();
		User user2 = new User();
		List<User> expectedUsers = Arrays.asList(user1, user2);
		when(userRepository.findNonTerminatedUsersWithNoCurrentBorrows(anyLong())).thenReturn(expectedUsers);

		List<User> result = userService.nonTerminatedUsersWithNoCurrentBorrows();

		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		assertTrue(result.containsAll(expectedUsers));
	}

	@Test
	public void whenNoNonTerminatedUsersWithNoCurrentBorrows_thenReturnEmptyList() {
		when(userRepository.findNonTerminatedUsersWithNoCurrentBorrows(anyLong())).thenReturn(Collections.emptyList());

		List<User> result = userService.nonTerminatedUsersWithNoCurrentBorrows();

		assertTrue(result.isEmpty());
	}

	@Test
	public void whenUsersBorrowedBooksOnDate_thenReturnListOfUsers() {
		String date = "03/15/2023";
		User user1 = new User();
		User user2 = new User();
		List<User> expectedUsers = Arrays.asList(user1, user2);
		when(borrowRepository.findUsersWhoBorrowedBooksOnDate(anyLong())).thenReturn(expectedUsers);

		List<User> result = userService.getUsersWhoBorrowedBooksOnDate(date);

		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		assertTrue(result.containsAll(expectedUsers));
	}

	@Test
	public void whenNoUsersBorrowedBooksOnDate_thenReturnEmptyList() {
		String date = "03/15/2023";
		when(borrowRepository.findUsersWhoBorrowedBooksOnDate(anyLong())).thenReturn(Collections.emptyList());

		List<User> result = userService.getUsersWhoBorrowedBooksOnDate(date);

		assertTrue(result.isEmpty());
	}

	@Test
	public void whenGetUserByIdAndUserExists_thenReturnsUser() throws UserNotExistException {
		String userId = "123";
		User expectedUser = new User();
		when(userRepository.findByUserId(userId)).thenReturn(Optional.of(expectedUser));

		User result = userService.getUserById(userId);

		assertNotNull(result);
		assertEquals(expectedUser, result);
	}

	@Test
	public void whenGetUserByIdAndUserDoesNotExist_thenThrowsNotFoundException() {
		String userId = "nonExistingId";
		when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

		UserNotExistException thrown = assertThrows(UserNotExistException.class, () -> userService.getUserById(userId),
				"user not found");

		assertTrue(thrown.getMessage().contains("not found"));
	}
}
