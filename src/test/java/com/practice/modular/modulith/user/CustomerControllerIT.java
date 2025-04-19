package com.practice.modular.modulith.user;

import com.practice.modular.modulith.bookstore.user.Customer;
import com.practice.modular.modulith.bookstore.user.UserService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author avinash
 */
@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@Transactional
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("johnDoe", "password123", "john.doe@example.com", new Date());
        userService.saveUser(customer);
    }

    @Test
    void givenExistingCustomerId_whenGetCustomerById_thenReturnsCustomer() throws Exception {
        // Given
        final Long customerId = customer.getId();

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.username").value("johnDoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void givenNonexistentCustomerId_whenGetCustomerById_thenReturnsNotFound() throws Exception {
        // Given
        final Long customerId = 999L;

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
