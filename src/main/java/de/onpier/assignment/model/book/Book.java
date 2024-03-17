package de.onpier.assignment.model.book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.onpier.assignment.model.base.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "Books", indexes = { @Index(name = "TITLE_AUTHOR", columnList = "title, author") })
public class Book extends JpaBaseEntity {

	@NotNull
	@Column(name = "title")
	private String title;

	@NotNull
	@Column(name = "author")
	private String author;

	@NotNull
	@Enumerated
	@Column(name = "genre", columnDefinition = "smallint")
	private Genre genre;

	@NotNull
	@Column(name = "publisher")
	private String publisher;

}
