package de.onpier.assignment.model.borrow.dao;

import java.util.List;

import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.borrow.Borrow;
import de.onpier.assignment.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {


	@Query("SELECT b.book FROM Borrow b WHERE b.user.userId = :userId AND b.borrowedFrom >= :startDate AND  b.borrowedTo <= :endDate")
	List<Book> findBooksBorrowedByUserInDateRange(@Param("userId") String userId, @Param("startDate") Long startDate, @Param("endDate") Long endDate);

	@Query("SELECT b.user FROM Borrow b WHERE b.borrowedFrom <= :date AND  b.borrowedTo >= :date")
	List<User> findUsersWhoBorrowedBooksOnDate(@Param("date") Long date);
}
