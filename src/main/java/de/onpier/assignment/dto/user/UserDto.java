package de.onpier.assignment.dto.user;

import java.time.LocalDate;

import de.onpier.assignment.model.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class UserDto {

	private String name;

	private String firstName;

	private LocalDate memberSince;

	private LocalDate memberTill;

	private Gender gender;
}
