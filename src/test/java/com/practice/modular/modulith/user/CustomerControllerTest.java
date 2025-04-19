package com.practice.modular.modulith.user;

import com.practice.modular.modulith.bookstore.user.Customer;
import com.practice.modular.modulith.bookstore.user.CustomerController;
import com.practice.modular.modulith.bookstore.user.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author avinash
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() throws Exception {
        // Given
        final Long customerId = 1L;
        final Customer customer = new Customer();
        customer.setId(customerId);
        customer.setUsername("johnDoe");
        customer.setEmail("john.doe@example.com");

        given(userService.findCustomerById(customerId)).willReturn(Optional.of(customer));

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.username").value("johnDoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getCustomerById_WhenCustomerDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        final Long customerId = 1L;
        given(userService.findCustomerById(customerId)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }
}
