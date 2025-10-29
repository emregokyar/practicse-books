package com.books.contoller;

import com.books.dto.BookDto;
import com.books.entity.Book;
import com.books.exception.BookNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Tag, returns the main info about this specific controller
@Tag(name = "Books Rest API Endpoints")
@RestController
@RequestMapping("/books")
public class BookController {
    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initialize();
    }

    private void initialize() {
        Book book1 = new Book(1, "a", "a", "a", 10);
        Book book2 = new Book(2, "b", "b", "b", 7);
        Book book3 = new Book(3, "c", "c", "c", 8);
        Book book4 = new Book(4, "d", "d", "a", 9);

        books.addAll(List.of(book1, book2, book3, book4));
    }

    private Book convertToBook(long id, BookDto bookDto) {
        return new Book(id, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getCategory(), bookDto.getRating());
    }

    public void addBook(Book book) {
        books.add(book);
    }

    //Operation annotation, returns info about CRUD operations
    @Operation(summary = "Get all books", description = "Retrieve the list of books")
    //Response status, returns status code if operation successful
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks() {
        return books;
    }

    @Operation(summary = "Get book by id", description = "Retrieve a specific book by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookById(@Parameter(description = "Id of the book")
                            @PathVariable @Min(value = 1) long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book not found with and id: " + id));
    }

    @Operation(summary = "Filter books", description = "Filter books by category")
    @GetMapping("/filter")
    public List<Book> filterByCategory(@Parameter(description = "Optional query parameter")
                                       @RequestParam(required = false) String category) {
        if (category == null) return books;

        return books.stream().filter(book ->
                book.getCategory().equalsIgnoreCase(category)
        ).toList();
    }

    @Operation(summary = "Create a book")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public void createBook(@Valid @RequestBody BookDto bookDto) {
        long id = books.isEmpty() ? 1 : (books.get(books.size() - 1).getId() + 1);
        Book newBook = convertToBook(id, bookDto);
        books.add(newBook);
    }

    @Operation(summary = "Update a book", description = "Update a book by specific id")
    @PutMapping("/{id}")
    public Book updateBook(@Parameter(description = "Id the of the book to update")
                           @PathVariable @Min(value = 1) long id,
                           @Valid @RequestBody BookDto bookDto) {
        /*
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .ifPresent(foundBook -> books.set(books.indexOf(foundBook), convertToBook(id, bookDto)));,
         */

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                Book updatedBook = convertToBook(id, bookDto);
                books.set(i, updatedBook);
                return updatedBook;
            }
        }
        throw new BookNotFoundException("Book not exists with an id: " + id);
    }

    @Operation(summary = "Delete a book", description = "Deleting a book by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@Parameter(description = "Id the of the book to delete")
                           @PathVariable @Min(value = 1) long id) {
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                //.ifPresent(books::remove)
                .orElseThrow(() -> new BookNotFoundException("Book not found with an id: " + id));
        books.removeIf(book -> book.getId() == id);
    }
}
