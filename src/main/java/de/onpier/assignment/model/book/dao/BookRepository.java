package de.onpier.assignment.model.book.dao;

import java.util.List;
import java.util.Optional;

import de.onpier.assignment.model.book.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByTitle(String title);

	@Query("SELECT b FROM Book b WHERE b.id NOT IN (SELECT br.book.id FROM Borrow br WHERE br.borrowedTo IS NULL OR br.borrowedTo > :toDate)")
	List<Book> findAvailableBooks(@Param("toDate") Long toDate);
}
