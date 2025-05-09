package com.practice.modular.modulith.inventory;

import com.practice.modular.modulith.bookstore.inventory.Inventory;
import com.practice.modular.modulith.bookstore.inventory.InventoryController;
import com.practice.modular.modulith.bookstore.inventory.InventoryService;
import com.practice.modular.modulith.bookstore.product.Book;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * @author avinash
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setBook(new Book()); // Assuming there's a constructor or setter to set up a Book
        inventory.setQuantity(100);
    }

    @Test
    void getAllInventories_ShouldReturnAllInventories() throws Exception {
        final List<Inventory> inventories = Arrays.asList(inventory);
        given(inventoryService.findAll()).willReturn(inventories);

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(inventory.getId()))
                .andExpect(jsonPath("$[0].quantity").value(inventory.getQuantity()));
    }

    @Test
    void getInventory_ShouldReturnInventory() throws Exception {
        given(inventoryService.findInventoryById(1L)).willReturn(inventory);

        mockMvc.perform(get("/api/inventory/{inventoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void addStock_ShouldUpdateInventory() throws Exception {
        // Setup the expected behavior for adding stock
        final Inventory updatedInventory = new Inventory(inventory.getId(), inventory.getBook(), inventory.getQuantity() + 10);
        given(inventoryService.addStock(1L, 10)).willReturn(updatedInventory);

        mockMvc.perform(post("/api/inventory/{inventoryId}/add", 1)
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(110)); // Expect updated quantity
    }

    @Test
    void removeStock_ShouldUpdateInventory() throws Exception {
        // Setup the expected behavior for removing stock
        final Inventory updatedInventory = new Inventory(inventory.getId(), inventory.getBook(), inventory.getQuantity() - 5);
        given(inventoryService.removeStock(1L, 5)).willReturn(updatedInventory);

        mockMvc.perform(post("/api/inventory/{inventoryId}/remove", 1)
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(95)); // Expect updated quantity
    }
}
