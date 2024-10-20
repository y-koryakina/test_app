package com.test.crm_app;

import com.test.crm_app.model.Seller;
import com.test.crm_app.model.Transaction;
import com.test.crm_app.model.PaymentType;
import com.test.crm_app.repository.SellerRepository;
import com.test.crm_app.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Чтобы тесты откатывали изменения в БД после выполнения
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testGetAllSellers() throws Exception {
        // Добавляем тестовых продавцов в базу данных
        sellerRepository.save(new Seller("John Doe", "john@example.com", LocalDateTime.of(2024, 1, 1, 10, 0)));
        sellerRepository.save(new Seller("Jane Smith", "jane@example.com", LocalDateTime.of(2024, 1, 1, 11, 0)));

        mockMvc.perform(get("/sellers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.name == 'John Doe')].contactInfo").value("john@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.name == 'Jane Smith')].contactInfo").value("jane@example.com"));
    }
    @Test
    public void testCreateSeller() throws Exception {
        String sellerJson = "{\"name\":\"John Doe\", \"contactInfo\":\"john@example.com\", \"registrationDate\":\"2024-01-01T10:00:00\"}";

        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sellerJson))
                .andExpect(status().isCreated());

        Seller savedSeller = sellerRepository.findByContactInfo("john@example.com");
        assertThat(savedSeller).isNotNull();
        assertThat(savedSeller.getName()).isEqualTo("John Doe");
        assertThat(savedSeller.getContactInfo()).isEqualTo("john@example.com");
    }



    @Test
    public void testGetSellerById() throws Exception {
        Seller seller = sellerRepository.save(new Seller("John Doe", "john@example.com", LocalDateTime.of(2024, 1, 1, 10, 0)));

        mockMvc.perform(get("/sellers/{id}", seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactInfo").value("john@example.com"));
    }

    @Test
    public void testUpdateSeller() throws Exception {
        String updatedSellerJson = "{\"name\":\"Jane Doe\", \"contactInfo\":\"jane@example.com\", \"registrationDate\":\"2024-01-01T10:00:00\"}";

        Seller seller = sellerRepository.save(new Seller("John Doe", "john@example.com", LocalDateTime.of(2024, 1, 1, 10, 0)));

        mockMvc.perform(put("/sellers/{id}", seller.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSellerJson))
                .andExpect(status().isOk());

        Seller updatedSeller = sellerRepository.findById(seller.getId()).orElse(null);
        assertNotNull(updatedSeller);
        assertEquals("Jane Doe", updatedSeller.getName());
        assertEquals("jane@example.com", updatedSeller.getContactInfo());
    }
}
