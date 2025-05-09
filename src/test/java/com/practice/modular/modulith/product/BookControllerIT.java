package com.practice.modular.modulith.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.modular.modulith.bookstore.product.Author;
import com.practice.modular.modulith.bookstore.product.AuthorRepository;
import com.practice.modular.modulith.bookstore.product.Book;
import com.practice.modular.modulith.bookstore.product.BookRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author avinash
 */
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@Transactional
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;

    private Author author;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();

        author = new Author();
        author.setName("John Doe");
        author = authorRepository.saveAndFlush(author);  // Ensure the author is persisted

        book = new Book();
        book.setTitle("Effective Java");
        book.setIsbn("9780134685991");
        book.setPrice(new BigDecimal("40.00"));
        book.setAuthor(author);
        book = bookRepository.saveAndFlush(book);
    }

    @Test
    void getAllBookSummaries_ShouldReturnSummaries() throws Exception {
        mockMvc.perform(get("/api/books/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Effective Java")))
                .andExpect(jsonPath("$[0].authorName", is("John Doe")))
                .andExpect(jsonPath("$[0].price", is(40.0)));
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.isbn", is(book.getIsbn())));
    }

    @Test
    void createBook_ShouldCreateAndReturnBook() throws Exception {
        final Book newBook = new Book();
        newBook.setTitle("Spring in Action");
        newBook.setIsbn("9780326204260");
        newBook.setPrice(new BigDecimal("45.00"));
        newBook.setAuthor(author); // reuse existing author

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Spring in Action")))
                .andExpect(jsonPath("$.isbn", is("9780326204260")));
    }

    @Test
    void updateBook_ShouldUpdateAndReturnBook() throws Exception {
        book.setTitle("Effective Java 3rd Edition");

        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Effective Java 3rd Edition")));
    }

    @Test
    void updateNonExistingBook_ShouldThrowNotFoundException() throws Exception {
        // Prepare a book object with changes
        final Book updatedBook = new Book();
        updatedBook.setTitle("Non Existing Title");
        updatedBook.setIsbn("0000000000");
        updatedBook.setPrice(new BigDecimal("50.00"));
        updatedBook.setAuthor(author);

        // Convert the book object to JSON
        final String bookJson = objectMapper.writeValueAsString(updatedBook);

        // Attempt to update a non-existing book
        mockMvc.perform(put("/api/books/{id}", Long.MAX_VALUE) // Use a very high value for ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound()); // Expecting HTTP 404 Not Found
    }

    @Test
    void deleteBook_ShouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", book.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isNotFound());
    }
}
