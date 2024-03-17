package de.onpier.assignment.service.borrow.impl;

import java.util.List;

import de.onpier.assignment.dto.borrow.BorrowedDto;
import de.onpier.assignment.exception.BookNotFoundException;
import de.onpier.assignment.exception.UserNotExistException;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.borrow.dao.BorrowRepository;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.service.book.BookService;
import de.onpier.assignment.service.borrow.BorrowService;
import de.onpier.assignment.service.borrow.mapper.BorrowServiceMapper;
import de.onpier.assignment.service.user.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

	private final BorrowRepository borrowRepository;

	private final BookService bookService;

	private final UserService userService;

	private final BorrowServiceMapper borrowServiceMapper;

	@Override
	public void saveBorrow(BorrowedDto borrowedDto) throws UserNotExistException, BookNotFoundException, NotFoundException {
		log.info("borrow ganna save with information:{}", borrowedDto);
		User user = userService.findByUserFullName(borrowedDto.getBorrowerName(), borrowedDto.getBorrowerFirstName());
		Book book = bookService.findByTitle(borrowedDto.getBookTitle());
		Borrow borrow = borrowServiceMapper.toBorrow(user, book, borrowedDto.getBorrowerFrom(), borrowedDto.getBorrowerTo());
		borrowRepository.save(borrow);
	}

}
