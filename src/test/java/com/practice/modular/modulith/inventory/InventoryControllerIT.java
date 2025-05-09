package com.practice.modular.modulith.inventory;

import com.practice.modular.modulith.bookstore.inventory.Inventory;
import com.practice.modular.modulith.bookstore.inventory.InventoryRepository;
import com.practice.modular.modulith.bookstore.product.Author;
import com.practice.modular.modulith.bookstore.product.AuthorRepository;
import com.practice.modular.modulith.bookstore.product.Book;
import com.practice.modular.modulith.bookstore.product.BookRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author avinash
 */
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@Transactional
class InventoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;
    private Author author;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        // Clear the repositories
        inventoryRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // Create and save author
        author = new Author("John Doe");
        authorRepository.save(author);

        // Create and save book
        book = new Book();
        book.setTitle("Effective Java");
        book.setIsbn("9780134685991");
        book.setPrice(new BigDecimal("40.00"));
        book.setAuthor(author);
        bookRepository.save(book);

        // Create and save inventory
        inventory = new Inventory();
        inventory.setBook(book);
        inventory.setQuantity(100);
        inventoryRepository.save(inventory);
    }

    @Test
    void getAllInventories_ShouldReturnAllInventories() throws Exception {
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].quantity").value(100));
    }

    @Test
    void getInventory_ShouldReturnInventory() throws Exception {
        mockMvc.perform(get("/api/inventory/{inventoryId}", inventory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void addStock_ShouldIncreaseInventory() throws Exception {
        mockMvc.perform(post("/api/inventory/{inventoryId}/add", inventory.getId())
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(110)); // Assumes inventory updates to 110
    }

    @Test
    void removeStock_ShouldDecreaseInventory() throws Exception {
        mockMvc.perform(post("/api/inventory/{inventoryId}/remove", inventory.getId())
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(80)); // Assumes inventory updates to 80
    }
}
