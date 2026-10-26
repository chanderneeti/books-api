package com.chander.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BooksApiIT {
    @Autowired
    MockMvc mvc;
    @Test
    void createThenFetch() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Clean Code\",\"author\":\"Robert Martin\",\"year\":2008}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        mvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }
}