package de.onpier.assignment.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.onpier.assignment.model.base.JpaBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users", indexes = { @Index(name = "NAME_FIRST_NAME", columnList = "name, first_name") })
public class User extends JpaBaseEntity {


	@NotNull
	@Column(name = "userId")
	private String userId;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "first_name")
	private String firstName;

	@NotNull
	@Column(name = "member_since")
	private Long memberSince;


	@Column(name = "member_till")
	private Long memberTill;

	@NotNull
	@Enumerated
	@Column(name = "gender", columnDefinition = "smallint")
	private Gender gender;
}
