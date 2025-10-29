package com.books.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookDto {
    @Size(min = 1, max = 30, message = "Rating should be between 1 and 30 characters.")
    private String title;

    @Size(min = 1, max = 40, message = "Rating should be between 1 and 40 characters.")
    private String author;

    @Size(min = 1, max = 30, message = "Rating should be between 1 and 30 characters.")
    private String category;

    @Min(value = 1, message = "Rating should be at least 1.")
    @Max(value = 10, message = "Max rating is 10.")
    private int rating;

    public BookDto(String title, String author, String category, int rating) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
