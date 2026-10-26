package com.chander.books;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long id) { super("Book " + id + " not found"); }
}
