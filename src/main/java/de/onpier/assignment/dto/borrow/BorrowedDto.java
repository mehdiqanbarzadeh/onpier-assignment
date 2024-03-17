package de.onpier.assignment.dto.borrow;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BorrowedDto {

	private String BorrowerName;

	private String BorrowerFirstName;

	private String bookTitle;

	private LocalDate BorrowerFrom;

	private LocalDate BorrowerTo;

}
