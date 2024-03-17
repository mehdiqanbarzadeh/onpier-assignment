# Onpier Application API

This Onpier Application provides a REST API to manage books, users, and borrowing records within a library system. It allows for querying users by their borrowing status, finding available books, and tracking books borrowed by users within specific date ranges.

## Features

- **List Active Borrowers**: Returns all users who have currently borrowed at least one book.
- **List Non-Terminated Users Without Current Borrows**: Identifies users who are not terminated and have not borrowed any books currently.
- **Find Users Who Borrowed Books On a Specific Date**: Retrieves users who have borrowed a book on a given date.
- **List Books Borrowed By a User Within Date Range**: Shows all books borrowed by a specified user in a given date range.
- **List Available Books**: Lists all books that are currently not borrowed and available for borrowing.

## Getting Started

### Prerequisites

- Java 17
- Maven
- Any IDE that supports Java (IntelliJ IDEA, Eclipse, VSCode)

### Installation

1. **Clone the repository**

```bash
git clone <repository-url>
```

2. **Navigate to the project directory**

```bash
cd onpier
```

3. **Build the project with Maven**

```bash
mvn clean install
```

4. **Run the Spring Boot application**

```bash
mvn spring-boot:run
```

The application should now be running and accessible via `http://localhost:8080`.

## Usage

The API endpoints can be accessed as follows:

### 1. List Active Borrowers

```
GET /api/borrowers/active
```

### 2. List Non-Terminated Users Without Current Borrows

```
GET /api/users/non-terminated
```

### 3. Find Users Who Borrowed Books On a Specific Date

```
GET /api/borrowers/date?date=YYYY-MM-DD
```

### 4. List Books Borrowed By a User Within Date Range

```
GET /api/books/borrowed?userId=<USER_ID>&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
```

### 5. List Available Books

```
GET /api/books/available
```

## Data Import

To import data from CSV files, ensure you have placed `users.csv`, `books.csv`, and `borrowed.csv` in the `src/main/resources` directory. The application automatically loads this data upon startup.

## Contributing

We welcome contributions! Please open an issue or submit a pull request for any features, bug fixes, or improvements.

## License

Distributed under the MIT License. See `LICENSE` for more information.
