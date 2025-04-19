package com.practice.modular.modulith.bookstore.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author avinash
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
