package com.practice.modular.modulith.bookstore.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author avinash
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
