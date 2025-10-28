package com.books.contoller;

import com.books.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initialize();
    }

    private void initialize() {
        Book book1 = new Book("a", "a", "a");
        Book book2 = new Book("b", "b", "b");
        Book book3 = new Book("c", "c", "c");
        Book book4 = new Book("d", "d", "a");

        books.addAll(List.of(book1, book2, book3, book4));
    }

    @GetMapping
    public List<Book> getBooks() {
        return books;
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable("bookId") Integer bookId) {
        return books.get(bookId);
    }

    @GetMapping("/title/{title}")
    public Book getBookByTitle(@PathVariable String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    @GetMapping("/name/{bookName}")
    public Book getBookByName(@PathVariable("bookName") String bookName) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(bookName))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/filter")
    public List<Book> getBookByCategory(@RequestParam(required = false) String category) {
        if (category == null) return books;

        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    @GetMapping("/find")
    public List<Book> getFoundBook(@RequestParam(required = false) String category) {
        if (category == null) return books;

        return books.stream().filter(book ->
                book.getCategory().equalsIgnoreCase(category)
        ).toList();
    }

    @PostMapping("/add")
    public void createBook(@RequestBody Book newBook) {
        var isMatched = books.stream()
                .anyMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));
        if (!isMatched) books.add(newBook);
    }

    @PutMapping("/update/{title}")
    public void updateBook(@PathVariable String title, @RequestBody Book updatedBook) {
        books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst().ifPresent(foundBook -> books.set(books.indexOf(foundBook), updatedBook));
    }

    @DeleteMapping("/delete/{title}")
    public void deleteBook(@PathVariable String title) {
        books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .ifPresent(books::remove);
    }
}
