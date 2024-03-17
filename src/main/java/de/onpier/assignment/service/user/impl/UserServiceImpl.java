package de.onpier.assignment.service.user.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import de.onpier.assignment.dto.user.UserDto;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.model.user.dao.UserRepository;
import de.onpier.assignment.service.user.UserService;
import de.onpier.assignment.service.user.mapper.UserServiceMapper;
import de.onpier.assignment.util.Utils;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserServiceMapper userServiceMapper;

	private final BorrowRepository borrowRepository;


	@Override
	public void saveUser(UserDto userDto) {
		log.info("gonna save user with Name:{} ", userDto.getName());
		try {
			userRepository.save(userServiceMapper.toUser(userDto));
		} catch (Exception exception) {
			log.error("there is a problem with this User:{}", userDto);
			log.error(exception.getMessage());
		}
	}

	@Override
	public User findByUserFullName(String borrowerName, String borrowerFirstName) throws UserNotExistException {
		log.info("find User with name:{},firstName{}", borrowerName, borrowerFirstName);
		if (borrowerName == null || borrowerFirstName == null || borrowerFirstName.isBlank() || borrowerName.isBlank()) {
			throw new IllegalArgumentException("invalid parameters, name and first name must be initiate!");
		}
		return userRepository.findByNameAndFirstName(borrowerName, borrowerFirstName)
				.orElseThrow(() -> new UserNotExistException("user not found!!!"));
	}

	@Override
	public List<User> getActiveUsers() {
		//TODO: it's not safe to retrieve all objects from db, please use pagination or any criteria to filtering some specific data from db
		return borrowRepository.findAll().stream().map(Borrow::getUser).toList();
	}

	@Override
	public List<User> nonTerminatedUsersWithNoCurrentBorrows() {
		//TODO: it's not safe to retrieve all objects from db, please use pagination or any criteria to filtering some specific data from db
		return userRepository.findNonTerminatedUsersWithNoCurrentBorrows(Utils.localDateToLong(LocalDate.now()));
	}

	@Override
	public List<User> getUsersWhoBorrowedBooksOnDate(String date) {
		return borrowRepository.findUsersWhoBorrowedBooksOnDate(Utils.localDateToLong(Utils.toLocalDate(date)));
	}

	@Override
	public User getUserById(String userId) throws UserNotExistException {
		return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotExistException("user not found"));
	}
}
