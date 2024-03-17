package de.onpier.assignment.model.borrow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.onpier.assignment.model.base.JpaBaseEntity;
import de.onpier.assignment.model.book.Book;
import de.onpier.assignment.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Borrows", indexes = { @Index(name = "USER", columnList = "user_id"), @Index(name = "BOOK", columnList = "book_id") })
public class Borrow extends JpaBaseEntity {

	@ManyToOne
	private User user;

	@ManyToOne
	private Book book;

	@NotNull
	@Column(name = "borrowed_from")
	private Long borrowedFrom;

	@Column(name = "borrowed_to")
	private Long borrowedTo;

}
