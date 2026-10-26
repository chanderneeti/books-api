package com.chander.books;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public Book create (BookRequest r) {
        long id = ids.incrementAndGet();
        Book book = new Book(id, r.title(), r.author(), r.year());
        books.put(id, book);
        return book;
    }

    public Book get (long id) {
        Book book = books.get(id);
        if (Objects.isNull(book)) throw new BookNotFoundException(id);
        return book;
    }

    public List<Book> list() {
        return books.values().stream()
                             .sorted(Comparator.comparing(Book::id))
                             .toList();
    }
}
