package com.chander.books;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class BooksApiIT {

    @Autowired
    WebApplicationContext context;
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())      // installs the security filter chain and @WithMockUser support
                .build();
    }


    @Test @WithMockUser(roles = "ADMIN")
    void createThenFetch() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Clean Code\",\"author\":\"Robert Martin\",\"year\":2008}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        mvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void anonymousGets401() throws Exception {
        mvc.perform(get("/api/books")).andExpect(status().isUnauthorized());
    }

    @Test @WithMockUser(roles = "USER")
    void userCannotCreate() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"author\":\"A\",\"year\":2000}"))
                .andExpect(status().isForbidden());
    }

    @Test @WithMockUser(roles = "ADMIN")
    void adminCanCreate() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"author\":\"A\",\"year\":2000}"))
                .andExpect(status().isCreated());
    }
}