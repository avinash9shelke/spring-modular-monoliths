package com.practice.modular.modulith.inventory;

import com.practice.modular.modulith.bookstore.inventory.Inventory;
import com.practice.modular.modulith.bookstore.inventory.InventoryRepository;
import com.practice.modular.modulith.bookstore.product.Author;
import com.practice.modular.modulith.bookstore.product.Book;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author avinash
 */
@DataJpaTest
@ActiveProfiles("test")
class InventoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void whenSaveInventory_thenFindById() {
        // Create and set up a new Author and Book for the test
        final Author author = new Author();
        author.setName("Jane Austen");
        entityManager.persist(author);

        final Book book = new Book();
        book.setTitle("Pride and Prejudice");
        book.setIsbn("1234567890123");
        book.setPrice(new BigDecimal("19.99"));
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save the inventory
        final Inventory inventory = new Inventory();
        inventory.setBook(book);
        inventory.setQuantity(150);
        inventoryRepository.save(inventory);

        // Flush to database and clear the persistence context
        entityManager.flush();
        entityManager.clear();

        // Retrieve the inventory
        final Inventory found = inventoryRepository.findById(inventory.getId()).orElse(null);

        // Assert the state of the retrieved inventory
        assertThat(found).isNotNull();
        assertThat(found.getQuantity()).isEqualTo(150);
        assertThat(found.getBook().getTitle()).isEqualTo("Pride and Prejudice");
    }

    @Test
    void whenDeleteInventory_thenCannotFindById() {
        // Create and set up a new Author and Book for the test
        final Author author = new Author();
        author.setName("Charles Dickens");
        entityManager.persist(author);

        final Book book = new Book();
        book.setTitle("Great Expectations");
        book.setIsbn("9876543210123");
        book.setPrice(new BigDecimal("15.99"));
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save the inventory
        final Inventory inventory = new Inventory();
        inventory.setBook(book);
        inventory.setQuantity(100);
        assertThat(inventory.getId()).isNull();
        inventoryRepository.save(inventory);

        // Delete the inventory
        inventoryRepository.delete(inventory);

        // Flush to database and clear the persistence context
        entityManager.flush();
        entityManager.clear();

        assertThat(inventory.getId()).isNotNull();
        // Assert that the inventory no longer exists
        final Inventory found = inventoryRepository.findById(inventory.getId()).orElse(null);
        assertThat(found).isNull();
    }
}
