package com.practice.modular.modulith.product;

/**
 * @author avinash
 */

import com.practice.modular.modulith.bookstore.product.Author;
import com.practice.modular.modulith.bookstore.product.AuthorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testSaveAuthor() {
        final Author author = new Author("John Doe");
        authorRepository.save(author);

        assertThat(author.getId()).isNotNull();
        assertThat(author.getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindById() {
        final Author author = new Author("Jane Doe");
        authorRepository.save(author);

        final Optional<Author> found = authorRepository.findById(author.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testFindAllAuthors() {
        final Author author1 = new Author("Alice Smith");
        final Author author2 = new Author("Bob Johnson");
        authorRepository.save(author1);
        authorRepository.save(author2);

        final List<Author> authors = authorRepository.findAll();

        assertThat(authors).hasSize(2);
        assertThat(authors).extracting(Author::getName).containsExactlyInAnyOrder("Alice Smith", "Bob Johnson");
    }

    @Test
    void testDeleteAuthor() {
        final Author author = new Author("Charlie Brown");
        authorRepository.save(author);

        assertThat(authorRepository.findById(author.getId())).isPresent();

        authorRepository.deleteById(author.getId());

        assertThat(authorRepository.findById(author.getId())).isNotPresent();
    }

    @Test
    void testSaveAuthorWithDuplicateNameShouldFail() {
        final Author author1 = new Author("David Goliath");
        authorRepository.save(author1);

        final Author author2 = new Author("David Goliath");

        // Expect a failure due to a unique constraint violation, if name is set to be unique in the entity
        assertThatThrownBy(() -> authorRepository.saveAndFlush(author2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}