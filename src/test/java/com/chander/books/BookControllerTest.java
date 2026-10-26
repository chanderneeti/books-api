package com.chander.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Boot 3.4+
// older Boot: import org.springframework.boot.test.mock.mockito.MockBean; and use @MockBean
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BookService service;

    @Test
    void getReturnBooks () throws Exception {
        given(service.get(1L)).willReturn(new Book(1L, "Clean Code", "Robert Martin", 2008));
        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void missingBookIs404() throws Exception {
        given(service.get(9L)).willThrow(new BookNotFoundException(9L));
        mvc.perform(get("/api/books/9")).andExpect(status().isNotFound());
    }

    @Test
    void invalidRequestIs400() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"author\":\"x\",\"year\":1200}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

}
