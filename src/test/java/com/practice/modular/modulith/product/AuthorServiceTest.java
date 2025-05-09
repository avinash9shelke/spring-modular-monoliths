package com.practice.modular.modulith.product;

/**
 * @author avinash
 */

import com.practice.modular.modulith.bookstore.product.Author;
import com.practice.modular.modulith.bookstore.product.AuthorRepository;
import com.practice.modular.modulith.bookstore.product.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;


@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");
    }

    @Test
    void findAllAuthors_ReturnsListOfAuthors() {
        given(authorRepository.findAll()).willReturn(Arrays.asList(author));

        final List<Author> authors = authorService.findAllAuthors();

        assertThat(authors).isNotNull();
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    void findAuthorById_ExistingId_ReturnsAuthor() {
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        final Optional<Author> foundAuthor = authorService.findAuthorById(1L);

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void saveAuthor_SavesAndReturnsAuthor() {
        given(authorRepository.save(any(Author.class))).willReturn(author);

        final Author savedAuthor = authorService.saveAuthor(author);

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getName()).isEqualTo("John Doe");
    }

    @Test
    void deleteAuthor_DeletesById() {
        willDoNothing().given(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        then(authorRepository).should().deleteById(1L);
    }

    @Test
    void updateAuthor_ExistingId_UpdatesAndReturnsAuthor() {
        final Author updatedAuthor = new Author();
        updatedAuthor.setName("Jane Doe");

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorRepository.save(author)).willReturn(author);

        final Author result = authorService.updateAuthor(1L, updatedAuthor);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Jane Doe");
    }

    @Test
    void updateAuthor_ShouldThrowExceptionIfNotFound() {
        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.updateAuthor(1L, new Author()));
    }

}
