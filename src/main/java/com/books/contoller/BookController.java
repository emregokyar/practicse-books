package com.books.contoller;

import com.books.entity.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {
    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initialize();
    }

    private void initialize() {
        Book book1 = new Book("a", "a", "a");
        Book book2 = new Book("b", "b", "b");
        Book book3 = new Book("c", "c", "c");

        books.addAll(List.of(book1, book2, book3));
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }
}
