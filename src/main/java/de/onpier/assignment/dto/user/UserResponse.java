package de.onpier.assignment.dto.user;

import lombok.Data;

@Data
public class UserResponse {
	private String userId;

	private String name;

	private String firstName;

	private String gender;

	private String memberFrom;

	private String memberTill;
}
