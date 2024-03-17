package de.onpier.assignment.service.user;

import java.util.List;

import de.onpier.assignment.dto.user.UserDto;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.user.User;

public interface UserService {


	void saveUser(UserDto userDto);

	User findByUserFullName(String borrowerName, String borrowerFirstName) throws UserNotExistException;

	List<User> getActiveUsers();

	List<User> nonTerminatedUsersWithNoCurrentBorrows();

	List<User> getUsersWhoBorrowedBooksOnDate(String date);

	User getUserById(String userId) throws UserNotExistException;
}
