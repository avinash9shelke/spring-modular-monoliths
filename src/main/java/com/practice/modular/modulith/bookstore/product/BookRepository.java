package com.practice.modular.modulith.bookstore.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author avinash
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    // Fetching only necessary data using a JPQL query
    @Query("""
            SELECT new com.practice.modular.modulith.bookstore.product.BookSummary(
                b.title,
                a.name,
                b.price
            ) FROM Book b JOIN b.author a
            """)
    List<BookSummary> findAllBookSummaries();
}
