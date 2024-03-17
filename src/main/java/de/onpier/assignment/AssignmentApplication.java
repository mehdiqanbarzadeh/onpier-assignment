package de.onpier.assignment;

import java.util.List;

import de.onpier.assignment.dto.borrow.BorrowedDto;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.service.book.BookService;
import de.onpier.assignment.service.borrow.BorrowService;
import de.onpier.assignment.service.user.UserService;
import de.onpier.assignment.util.Utils;
import javassist.NotFoundException;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentApplication implements ApplicationRunner {

	private final UserService userService;

	private final BookService bookService;

	private final BorrowService borrowService;

	public AssignmentApplication(UserService userService,
			BookService bookService,
			BorrowService borrowService) {

		this.userService = userService;
		this.bookService = bookService;
		this.borrowService = borrowService;
	}


	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws UserNotExistException, NotFoundException {
		Utils.getUsers().forEach(userService::saveUser);
		Utils.getBooks().forEach(bookService::saveBook);
		List<BorrowedDto> borrowed = Utils.getBorrowed();
		for (BorrowedDto borrowedDto : borrowed) {
			borrowService.saveBorrow(borrowedDto);
		}
	}
}
