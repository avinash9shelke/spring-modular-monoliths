package com.practice.modular.modulith;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookStoreApplicationTests {

    @Test
    void applicationStarts() {
        Assertions.assertThatCode(() -> BookStoreApplication.main(new String[]{})).doesNotThrowAnyException();
    }

}
