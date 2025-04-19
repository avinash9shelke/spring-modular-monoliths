package com.practice.modular.modulith.bookstore.product;

import java.math.BigDecimal;

/**
 * @author avinash
 */
public record BookSummary(String title, String authorName, double price) {

    public BookSummary(String title, String authorName, BigDecimal price) {
        this(title, authorName, price.doubleValue());
    }

}
