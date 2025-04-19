package com.practice.modular.modulith.bookstore.orders;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author avinash
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
