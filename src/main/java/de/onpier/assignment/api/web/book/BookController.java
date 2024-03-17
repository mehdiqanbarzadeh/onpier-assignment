package de.onpier.assignment.api.web.book;

import java.util.List;

import de.onpier.assignment.api.web.book.mapper.BookControllerMapper;
import de.onpier.assignment.dto.book.BookResponse;
import de.onpier.assignment.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "Controller for managing books")
public class BookController {


	private final BookService bookService;

	private final BookControllerMapper bookControllerMapper;


	@GetMapping("/borrowed/by-user")
	@Operation(summary = "Get books borrowed by a user within a date range",
			description = "Returns a list of books that a user has borrowed within a specified date range",
			responses = {
					@ApiResponse(responseCode = "200", description = "Successful retrieval",
							content = @Content(schema = @Schema(implementation = BookResponse.class))),
					@ApiResponse(responseCode = "404", description = "User not found")
			})
	public ResponseEntity<List<BookResponse>> getBooksBorrowedByUserBetweenDates(
			@RequestParam("userId") String userId,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) throws NotFoundException {

		log.info("getting borrowed books by userId:{},from:{},to: {}", userId, startDate, endDate);
		return ResponseEntity.ok(bookControllerMapper.ToBookResponses(bookService.getBooksBorrowedByUserInDateRange(userId, startDate, endDate)));
	}

	@GetMapping("/available")
	@Operation(summary = "Get available books",
			description = "Returns a list of all books that are currently available for borrowing",
			responses = @ApiResponse(responseCode = "200", description = "Successful retrieval",
					content = @Content(schema = @Schema(implementation = BookResponse.class))))
	public ResponseEntity<List<BookResponse>> getAvailableBooks() {
		log.info("getting available books");
		return ResponseEntity.ok(bookControllerMapper.ToBookResponses(bookService.getAvailableBooks()));
	}

}
