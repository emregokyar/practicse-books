package com.books.controller;

import com.books.contoller.BookController;
import com.books.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookController bookController;

    @BeforeEach
    void setup() {
        bookController.getBooks().clear();
        bookController.addBook(new Book("a", "a", "a"));
        bookController.addBook(new Book("b", "b", "b"));
        bookController.addBook(new Book("c", "c", "c"));
        bookController.addBook(new Book("d", "d", "d"));
    }

    @Test
    @Order(0)
    void getAllBooksTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    @Order(1)
    void getBookByTitleTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("a")));
    }

    @Test
    @Order(2)
    void filterBooksTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/filter")
                        .param("category", "a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Order(3)
    void addingNewBookTest() throws Exception {
        Book newBook = new Book("e", "e", "e");
        mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(5, bookController.getBooks().size());
    }

    @Test
    @Order(4)
    void updateBookTest() throws Exception {
        Book updateBook = new Book("f", "f", "f");
        mockMvc.perform(MockMvcRequestBuilders.put("/books/b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBook)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        boolean res = bookController.getBooks().stream()
                .anyMatch(book -> book.getTitle().equalsIgnoreCase("b"));
        Assertions.assertFalse(res);
    }

    @Test
    @Order(5)
    void deleteBookTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/c"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(3, bookController.getBooks().size());
    }
}
