package de.onpier.assignment.service.borrow;

import de.onpier.assignment.dto.borrow.BorrowedDto;
import de.onpier.assignment.exception.UserNotExistException;
import javassist.NotFoundException;

public interface BorrowService {
	void saveBorrow(BorrowedDto borrowedDto) throws UserNotExistException, NotFoundException;

}
