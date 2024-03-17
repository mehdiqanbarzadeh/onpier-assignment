package de.onpier.assignment.model.user.dao;

import java.util.List;
import java.util.Optional;

import de.onpier.assignment.model.user.User;
import de.onpier.assignment.service.user.impl.UserServiceImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByNameAndFirstName(String name, String firstName);

	@Query("SELECT u FROM User u WHERE (u.memberTill IS NULL OR u.memberTill > :currentDate) " +
			"AND NOT EXISTS (" +
			"SELECT b FROM Borrow b WHERE b.user = u AND (b.borrowedTo IS NULL OR b.borrowedTo > :currentDate)" +
			")")
	List<User> findNonTerminatedUsersWithNoCurrentBorrows(@Param("currentDate") Long currentDate);

	Optional<User> findByUserId(String userId);

}
