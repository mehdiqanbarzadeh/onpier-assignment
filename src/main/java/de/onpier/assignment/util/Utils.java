package de.onpier.assignment.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.onpier.assignment.dto.book.BookDto;
import de.onpier.assignment.dto.borrow.BorrowedDto;
import de.onpier.assignment.dto.user.UserDto;
import de.onpier.assignment.model.book.Genre;
import de.onpier.assignment.model.user.Gender;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public static CSVParser processLocalFile(String path) {
		CSVParser csvParser = null;
		try {
			Reader reader = Files.newBufferedReader(Paths.get(path));
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvParser;
	}

	public static List<UserDto> getUsers() {
		logger.info("gonna process users and store them in db");
		List<UserDto> users = new ArrayList<>();
		CSVParser csvRecords = processLocalFile("src/main/resources/files/user.csv");
		for (CSVRecord csvRecord : csvRecords) {
			if (csvRecord.getRecordNumber() != 1) {
				UserDto userDto = UserDto
						.builder()
						.name(csvRecord.get(0))
						.firstName(csvRecord.get(1))
						.memberSince(toLocalDate(csvRecord.get(2)))
						.memberTill(toLocalDate(csvRecord.get(3)))
						.gender(Gender.of(csvRecord.get(4)))
						.build();
				users.add(userDto);
			}
		}
		return users;
	}

	public static List<BookDto> getBooks() {
		logger.info("gonna process books and store them in db");
		List<BookDto> books = new ArrayList<>();
		processLocalFile("src/main/resources/files/books.csv").forEach(csvRecord -> {
			if (csvRecord.getRecordNumber() != 1 && !isRecordEmpty(csvRecord)) {
				BookDto bookDto = BookDto
						.builder()
						.title(csvRecord.get(0))
						.author(csvRecord.get(1))
						.genre(Genre.of(csvRecord.get(2)))
						.publisher(csvRecord.get(3)).build();
				books.add(bookDto);
			}
		});
		return books;
	}

	private static boolean isRecordEmpty(CSVRecord csvRecord) {
		return (csvRecord.get(0).isBlank() && csvRecord.get(1).isBlank() && csvRecord.get(2).isBlank() && csvRecord.get(3).isBlank());
	}


	public static List<BorrowedDto> getBorrowed() {
		logger.info("gonna process borrower information and store them in db");
		List<BorrowedDto> borrows = new ArrayList<>();
		CSVParser csvRecords = processLocalFile("src/main/resources/files/borrowed.csv");
		for (CSVRecord csvRecord : csvRecords) {
			if (csvRecord.getRecordNumber() != 1) {
				BorrowedDto bookDto = BorrowedDto
						.builder()
						.BorrowerName(getBorrowerName(csvRecord))
						.BorrowerFirstName(getBorrowerFirstName(csvRecord))
						.bookTitle(csvRecord.get(1))
						.BorrowerFrom(toLocalDate(csvRecord.get(2)))
						.BorrowerTo(toLocalDate(csvRecord.get(3)))
						.build();
				borrows.add(bookDto);
			}
		}
		return borrows;
	}

	private static String getBorrowerName(CSVRecord csvRecord) {
		String[] split = csvRecord.get(0).split(",");
		return split[0];
	}

	private static String getBorrowerFirstName(CSVRecord csvRecord) {
		String[] split = csvRecord.get(0).split(",");
		return split[1];
	}

	public static LocalDate toLocalDate(String date) {
		return date.isBlank() ? null : LocalDate.parse(date, DATE_FORMATTER);
	}

	public static String localDateToString(Long localDate) {
		if (localDate == null) {
			return "N/A";
		}

		Instant instant = Instant.ofEpochMilli(localDate);
		return instant.atZone(ZoneId.systemDefault()).toLocalDate().format(DATE_FORMATTER);

	}

	public static Long localDateToLong(LocalDate endDate) {
		return endDate != null ? endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) : null;
	}


	public static String generateUniqueUserId() {
		return UUID.randomUUID().toString();
	}
}