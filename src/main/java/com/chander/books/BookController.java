package com.chander.books;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/api/books")
public class BookController {
    private final BookService bookService;
    public BookController (BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> create (@Valid  @RequestBody BookRequest request) {
        Book saved = bookService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public Book get (@PathVariable long id) {
        return bookService.get(id);
    }

    @GetMapping
    public List<Book> list () {
        return bookService.list();
    }

}
