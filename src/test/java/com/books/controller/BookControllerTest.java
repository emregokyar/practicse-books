package com.books.controller;

import com.books.contoller.BookController;
import com.books.dto.BookDto;
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

import java.util.List;

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
        Book book1 = new Book(1, "a", "a", "a", 10);
        Book book2 = new Book(2, "b", "b", "b", 7);
        Book book3 = new Book(3, "c", "c", "c", 8);
        Book book4 = new Book(4, "d", "d", "a", 9);

        bookController.getBooks().addAll(List.of(book1, book2, book3, book4));
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
    void getBookByIdSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("a")));
    }

    @Test
    @Order(2)
    void getBookByIdFailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/11"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Book not found with and id: 11")));
    }

    @Test
    @Order(3)
    void filterBooksTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/filter")
                        .param("category", "a"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Order(4)
    void addingNewBookTest() throws Exception {
        BookDto newBookDto = new BookDto("e", "e", "e", 10);
        mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertEquals(5, bookController.getBooks().size());
    }

    @Test
    @Order(5)
    void updateBookSuccessTest() throws Exception {
        BookDto beUpdatedBook = new BookDto("f", "f", "f", 10);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beUpdatedBook)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(beUpdatedBook.getTitle())));
    }

    @Test
    @Order(6)
    void updateBookFailTest() throws Exception {
        BookDto beUpdatedBook = new BookDto("f", "f", "f", 11);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beUpdatedBook)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid request")));
    }

    @Test
    @Order(7)
    void updateBookFailTestByInvalidId() throws Exception {
        BookDto beUpdatedBook = new BookDto("f", "f", "f", 10);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beUpdatedBook)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Book not exists with an id: 15")));
    }

    @Test
    @Order(8)
    void deleteBookSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertEquals(3, bookController.getBooks().size());
    }

    @Test
    @Order(8)
    void deleteBookFailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/145"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Book not found with an id: 145")));
    }
}
