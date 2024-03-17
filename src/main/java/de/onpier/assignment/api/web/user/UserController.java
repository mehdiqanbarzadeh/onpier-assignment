package de.onpier.assignment.api.web.user;

import java.util.List;

import de.onpier.assignment.api.web.user.mapper.UserControllerMapper;
import de.onpier.assignment.dto.user.UserResponse;
import de.onpier.assignment.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Controller for managing users")
public class UserController {


	private final UserService userService;

	private final UserControllerMapper userControllerMapper;

	@GetMapping("/actives")
	@Operation(summary = "Get active users",
			description = "Retrieves all active users.",
			responses = @ApiResponse(responseCode = "200", description = "Successful retrieval",
					content = @Content(schema = @Schema(implementation = UserResponse.class))))
	public ResponseEntity<List<UserResponse>> getActiveUsers() {
		log.info("ganna retrieve all active users");
		return ResponseEntity.ok(userControllerMapper.toUserResponse(userService.getActiveUsers()));
	}


	@GetMapping("/nonTerminate/withoutBorrows")
	@Operation(summary = "Get non-terminated users who have no current borrows",
			description = "Fetches non-terminated users who have no current borrows.",
			responses = @ApiResponse(responseCode = "200", description = "Successful retrieval",
					content = @Content(schema = @Schema(implementation = UserResponse.class))))
	public ResponseEntity<List<UserResponse>> nonTerminatedUsersWithNoCurrentBorrows() {
		log.info("fetch non terminated users with no current borrows");
		return ResponseEntity.ok(userControllerMapper.toUserResponse(userService.nonTerminatedUsersWithNoCurrentBorrows()));
	}


	@GetMapping("/borrowed")
	@Operation(summary = "Get users who borrowed books on a specific date",
			description = "Retrieves users who borrowed books on the specified date.",
			responses = @ApiResponse(responseCode = "200", description = "Successful retrieval",
					content = @Content(schema = @Schema(implementation = UserResponse.class))))
	public ResponseEntity<?> getUsersWhoBorrowedBooksOnDate(@Param("date") String date) {
		log.info("getting users who borrowed books on date:{}", date);
		return ResponseEntity.ok(userControllerMapper.toUserResponse(userService.getUsersWhoBorrowedBooksOnDate(date)));
	}

}
