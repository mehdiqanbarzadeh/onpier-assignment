package de.onpier.assignment.model.base;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class JpaBaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 9147304262683573000L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

}