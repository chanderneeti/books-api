package com.chander.books;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank(message = "title is required") String title,
        @NotBlank(message = "author is required") String author,
        @Min(value = 1450, message = "year must be 1450 or later")
        @Max(value = 2100, message = "year must be 2100 or earlier") int year) {
}
